package com.senzhikong.db.sql.wrapper;

import com.senzhikong.db.sql.CacheColumn;
import com.senzhikong.db.sql.CacheTable;
import com.senzhikong.db.sql.WrapperConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析器
 * @author shu
 */
@Component
public class WrapperParser {
    @Resource
    private WrapperConfig wrapperConfig;
    private static final Map<String, CacheTable> CACHE_TABLE_MAP = new HashMap<>();

    public CacheColumn getColumn(Class<?> clz, String field) {
        CacheColumn cacheColumn = getTable(clz).getColumns().get(field);
        if (cacheColumn == null) {
            throw new RuntimeException(String.format("类【%s】不存在%s属性", clz.getName(), field));
        }
        return cacheColumn;
    }

    private static final String MARK_CHAR = "`";

    public CacheTable getTable(Class<?> clz) {
        CacheTable cacheTable = CACHE_TABLE_MAP.get(clz.getName());
        if (cacheTable != null) {
            return cacheTable;
        }
        cacheTable = new CacheTable();
        String tableName = clz.getSimpleName();
        cacheTable.setClassName(tableName);
        cacheTable.setFullClass(clz.getName());
        cacheTable.setTableClass(clz);
        cacheTable.setName(tableName);
        Table table = clz.getDeclaredAnnotation(Table.class);
        if (table != null) {
            String name = table.name();
            String catalog = table.catalog();
            String schema = table.schema();
            if (StringUtils.isNotEmpty(name)) {
                tableName = name;
            }
            if (StringUtils.isNotEmpty(catalog)) {
                cacheTable.setCatalog(catalog);
            }
            if (StringUtils.isNotEmpty(schema)) {
                if (schema.startsWith(MARK_CHAR)) {
                    schema = schema.substring(1, schema.length() - 1);
                }
                cacheTable.setSchema(schema);
            }
        } else {
            if (StringUtils.equalsIgnoreCase(wrapperConfig.getNamingStrategy(), WrapperConfig.CAMEL)) {
                tableName = toCamelCase(tableName);
            } else if (StringUtils.equalsIgnoreCase(wrapperConfig.getNamingStrategy(), WrapperConfig.UNDER_LINE)) {
                tableName = toUnderlineCase(tableName);
            }
        }
        cacheTable.setName(tableName);
        cacheTable.setColumns(new HashMap<>(8));
        parseTableColumns(clz, cacheTable);
        CACHE_TABLE_MAP.put(cacheTable.getFullClass(), cacheTable);
        return cacheTable;
    }

    private void parseTableColumns(Class<?> clz, CacheTable cacheTable) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            CacheColumn cacheColumn = new CacheColumn();
            cacheColumn.setFieldName(field.getName());
            cacheColumn.setFieldClass(field.getType());
            cacheColumn.setFieldType(field.getType().getSimpleName());
            String columnName = field.getName();
            Column column = field.getDeclaredAnnotation(Column.class);
            if (column != null) {
                columnName = column.name();
                if (columnName.startsWith("`")) {
                    columnName = columnName.substring(1, columnName.length() - 1);
                }
            } else {
                if (StringUtils.equalsIgnoreCase(wrapperConfig.getNamingStrategy(), WrapperConfig.CAMEL)) {
                    columnName = toCamelCase(columnName);
                } else if (StringUtils.equalsIgnoreCase(wrapperConfig.getNamingStrategy(), WrapperConfig.UNDER_LINE)) {
                    columnName = toUnderlineCase(columnName);
                }
            }
            cacheColumn.setColumnName(columnName);
            cacheColumn.setTable(cacheTable);
            cacheTable.getColumns().put(cacheColumn.getFieldName(), cacheColumn);
        }
    }

    public static String methodToFieldName(String getName) {
        return getName.substring(3, 4).toLowerCase() +
                getName.substring(4);
    }

    public <T extends Serializable, S extends Serializable> CacheColumn getColumn(ObjectFunction<T, S> column) {
        try {

            Method method = column.getClass().getDeclaredMethod("writeReplace");
            AccessController.doPrivileged(
                    new SetAccessibleAction<>(method));
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(column);

            String methodName = serializedLambda.getImplMethodName();
            String className = serializedLambda.getImplClass();
            className = className.replaceAll("/", ".");
            String fieldName = methodToFieldName(methodName);
            Class<?> clz = Class.forName(className);
            return getColumn(clz, fieldName);

        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuilder buffer = new StringBuilder();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }
}
