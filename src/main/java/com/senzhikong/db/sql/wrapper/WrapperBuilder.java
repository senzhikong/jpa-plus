package com.senzhikong.db.sql.wrapper;

import com.senzhikong.db.sql.CacheColumn;
import com.senzhikong.db.sql.CacheTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WrapperBuilder {
    public static final String SELECT_PREFIX = "select_";
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    @Resource
    private WrapperParser wrapperParser;

    public String wrapperValueToText(WrapperValue wrapperValue, List<Object> params) {
        String valueText;
        ValueType type = wrapperValue.getType();
        switch (type) {
            case VALUE:
                Object value = wrapperValue.getValue();
                if (value instanceof Collection) {
                    value = ((Collection<?>) value).toArray();
                }
                if (value.getClass().isArray()) {
                    StringBuilder text = new StringBuilder();
                    text.append("(");
                    for (int i = 0; i < Array.getLength(value); i++) {
                        if (i > 0) {
                            text.append(",");
                        }
                        params.add(Array.get(value, i));
                        text.append("?").append(params.size());
                    }
                    text.append(")");
                    valueText = text.toString();
                    break;
                }
                params.add(wrapperValue.getValue());
                valueText = "?" + params.size();
                break;
            case COLUMN:
                valueText = wrapperValue.getColumn();
                break;
            case FUNCTION:
                CacheColumn cacheColumn = wrapperParser.getColumn(wrapperValue.getFunction());
                String tableName = cacheColumn.getTable().getAsName();
                if (wrapperValue.getFunctionClass() != null) {
                    tableName = wrapperValue.getFunctionClass().getSimpleName();
                }
                valueText = cacheColumn.getColumnName();
                if (StringUtils.isNotEmpty(tableName)) {
                    valueText = tableName + "." + valueText;
                }
                break;
            case WRAPPER:
                valueText = this.getWrapper(wrapperValue.getWrapper(), params);
                break;
            default:
                throw new RuntimeException("不支持的类型:" + type);
        }
        return valueText;
    }


    public String getWrappers(List<Wrapper> list, String concatStr,
            List<Object> params) {
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sql.append("\n");
                sql.append(concatStr);
                sql.append(" ");
            }
            Wrapper wrapper = list.get(i);
            sql.append(getWrapper(wrapper, params));
        }
        return sql.toString();
    }

    public String getWrapper(Wrapper wrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        WrapperType wrapperType = wrapper.getType();
        switch (wrapperType) {
            case EQ:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" = ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case NOT_EQ:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" != ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case GT:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" > ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case LT:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" < ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case GE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" >= ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case LE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" <= ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case IS_NULL:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" is null");
                break;
            case NOT_NULL:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params))
                   .append(" is not null");
                break;
            case START:
            case END:
            case REGEX:
            case LIKE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" like ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case IN:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" in ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case NOT_IN:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" not in ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case FUNCTION:
                sql.append(getFunctionWrapper(wrapper, params));
                break;
            case OR:
                if (wrapper instanceof ListWrapper) {
                    String or = getWrappers(((ListWrapper) wrapper).getWrapperList(), OR,
                            params);
                    sql.append("(").append(or).append(")");
                }
                break;
            case AND:
                String and = getWrappers(((ListWrapper) wrapper).getWrapperList(), AND, params);
                sql.append("(").append(and).append(")");
                break;
            default:
                break;
        }
        return sql.toString();
    }

    public String getFunctionWrapper(Wrapper wrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        Function function = wrapper.getFunction();
        List<String> valueTexts = wrapper.getValueList().stream().map(v -> wrapperValueToText(v, params)).collect(
                Collectors.toList());
        String functionParams = StringUtils.join(valueTexts, ",");
        switch (function) {
            case COUNT_DISTINCT:
                sql.append("COUNT( DISTINCT ").append(functionParams).append(")");
                break;
            case CUSTOMIZE:
                sql.append(wrapper.getFunctionName()).append("(").append(functionParams).append(")");
                break;
            case CUSTOMIZE_TEXT:
                String functionText = wrapper.getFunctionName();
                String[] arr = functionText.split("\\?");
                if (arr.length != valueTexts.size() + 1) {
                    throw new RuntimeException(
                            String.format("参数数量不符，应该是%d个，实际有%d个", arr.length - 1, valueTexts.size()));
                }
                valueTexts.add("");
                for (int i = 0; i < valueTexts.size(); i++) {
                    sql.append(arr[i]).append(valueTexts.get(i));
                }
                break;
            default:
                sql.append(function).append("(").append(functionParams).append(")");
                break;
        }
        return sql.toString();
    }

    public void buildSelect(StringBuilder sql, CacheTable cacheTable,
            List<SelectWrapper> selects, List<Object> params) {
        sql.append("SELECT ");
        if (selects == null || selects.isEmpty()) {
            sql.append(cacheTable.getAsName()).append(".* ");
            return;
        }
        for (int i = 0; i < selects.size(); i++) {
            SelectWrapper wrapper = selects.get(i);
            SelectType wrapperType = wrapper.getSelectType();
            if (i > 0) {
                sql.append(",");
            }
            switch (wrapperType) {
                case COLUMN:
                    sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
                    break;
                case FUNCTION:
                    sql.append(getFunctionWrapper(wrapper, params));
                    break;
                default:
                    break;
            }
            sql.append(" AS ");
            if (StringUtils.isNotEmpty(wrapper.getAsName())) {
                sql.append(wrapper.getAsName());
            } else {
                sql.append(SELECT_PREFIX);
                sql.append(i);
            }
            sql.append(" ");
        }
    }

    public void buildFrom(StringBuilder sql, QueryWrapper<? extends Serializable> queryWrapper) {
        CacheTable cacheTable = wrapperParser.getTable(queryWrapper.getGenericsClass());
        sql.append(" FROM ");
        sql.append(cacheTable.getFullNameAs());
        sql.append(" ");

    }

    public void buildJoin(StringBuilder sql, List<JoinWrapper> list,
            List<Object> params) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (JoinWrapper join : list) {
            JoinType type = join.getJoinType();
            String joinType = "INNER JOIN";
            switch (type) {
                case RIGHT:
                    joinType = "RIGHT JOIN";
                    break;
                case LEFT:
                    joinType = "LEFT JOIN";
                    break;
                case OUTER:
                    joinType = "OUTER JOIN";
                    break;
                default:
                    break;
            }
            CacheTable cacheTable = wrapperParser.getTable(join.getGenericsClass());
            sql.append("\n");
            sql.append(joinType);
            sql.append(" ");
            sql.append(cacheTable.getFullNameAs());
            sql.append(" ON ");
            sql.append(getWrappers(join.getWrapperList(), AND, params));
            sql.append(" ");
        }
    }


    public void buildWhere(StringBuilder sql, List<Wrapper> wheres,
            List<Object> params) {
        if (wheres == null || wheres.isEmpty()) {
            return;
        }
        sql.append("\nWHERE ");
        sql.append(getWrappers(wheres, AND, params));
    }


    public void buildGroupBuy(StringBuilder sql, List<Wrapper> wrapperList,
            List<Object> params) {
        if (wrapperList == null || wrapperList.isEmpty()) {
            return;
        }
        sql.append("\nGROUP BY ");
        for (int i = 0; i < wrapperList.size(); i++) {
            Wrapper wrapper = wrapperList.get(i);
            if (i > 0) {
                sql.append(" , ");
            }
            sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
        }
    }

    public void buildOrder(StringBuilder sql, List<OrderByWrapper> wrapperList,
            List<Object> params) {
        if (wrapperList == null || wrapperList.isEmpty()) {
            return;
        }
        sql.append("\nORDER BY ");
        for (int i = 0; i < wrapperList.size(); i++) {
            OrderByWrapper wrapper = wrapperList.get(i);
            OrderByType wrapperType = wrapper.getOrderByType();
            if (i > 0) {
                sql.append(" , ");
            }
            sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
            switch (wrapperType) {
                case ASC:
                    sql.append(" ASC ");
                    break;
                case DESC:
                    sql.append(" DESC ");
                    break;
                default:
                    throw new RuntimeException("不支持的排序方式：" + wrapperType);
            }
        }
    }

    public void buildLimit(StringBuilder sql, PagerQueryWrapper<? extends Serializable> pagerQueryWrapper,
            List<Object> params) {
        sql.append("\nLIMIT ");
        params.add((pagerQueryWrapper.getPageNumber() - 1) * pagerQueryWrapper.getPageSize());
        sql.append("?").append(params.size());
        params.add(pagerQueryWrapper.getPageSize());
        sql.append(",?").append(params.size()).append(" ");
    }

    public String countByWrapper(QueryWrapper<?> queryWrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        buildFrom(sql, queryWrapper);
        buildJoin(sql, queryWrapper.getJoinWrappers(), params);
        buildWhere(sql, queryWrapper.getWrapperList(), params);
        buildGroupBuy(sql, queryWrapper.getGroupBys(), params);
        return sql.toString();
    }

    public String selectByWrapper(QueryWrapper<? extends Serializable> queryWrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        CacheTable cacheTable = wrapperParser.getTable(queryWrapper.getGenericsClass());
        buildSelect(sql, cacheTable, queryWrapper.getSelects(), params);
        buildFrom(sql, queryWrapper);
        buildJoin(sql, queryWrapper.getJoinWrappers(), params);
        buildWhere(sql, queryWrapper.getWrapperList(), params);
        buildGroupBuy(sql, queryWrapper.getGroupBys(), params);
        buildOrder(sql, queryWrapper.getOrderBys(), params);
        if (queryWrapper instanceof PagerQueryWrapper && ((PagerQueryWrapper<? extends Serializable>) queryWrapper).isPage()) {
            buildLimit(sql, (PagerQueryWrapper<? extends Serializable>) queryWrapper, params);
        }
        return sql.toString();
    }

}
