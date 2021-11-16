package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shu.zhou
 * @date 2018年10月24日下午3:10:01
 */
@Getter
@Setter
public class QueryWrapper<T extends Serializable> extends ListWrapper {

    List<SelectWrapper> selects = new ArrayList<>();
    List<Wrapper> groupBys = new ArrayList<>();
    List<OrderByWrapper> orderBys = new ArrayList<>();
    private boolean distinct = false;

    List<JoinWrapper> joinWrappers = new ArrayList<>();

    public <R extends Serializable, S extends Serializable> WrapperValue value(ObjectFunction<S, R> func) {
        WrapperValue wrapperValue = Wrapper.col(func);
        wrapperValue.setFunctionClass(genericsClass);
        return wrapperValue;
    }

    public QueryWrapper(Class<T> clz) {
        super();
        this.genericsClass = clz;
        this.setType(WrapperType.AND);
    }

    protected QueryWrapper() {
        this.setType(WrapperType.AND);
    }


    public static <T extends Serializable> QueryWrapper<T> from(Class<T> clz) {
        return new QueryWrapper<>(clz);
    }

    public static <T extends Serializable> QueryWrapper<T> from() {
        return new QueryWrapper<>();
    }


    public QueryWrapper<T> select(String key) {
        return select(Wrapper.col(key));
    }

    public <R extends Serializable, S extends Serializable> QueryWrapper<T> select(ObjectFunction<S, R> func) {
        return select(Wrapper.col(func));
    }

    public QueryWrapper<T> select(WrapperValue wrapperValue) {
        SelectWrapper selectWrapper = new SelectWrapper();
        selectWrapper.setSelectType(SelectType.COLUMN);
        selectWrapper.getValueList().add(wrapperValue);
        selects.add(selectWrapper);
        return this;
    }

    @SafeVarargs
    public final <R extends Serializable, S extends Serializable> QueryWrapper<T> selectFunc(Function function,
            ObjectFunction<T, S>... func) {
        List<WrapperValue> values = Arrays.stream(func).map(Wrapper::col).collect(Collectors.toList());
        return selectFunc(function, null, values);
    }

    @SafeVarargs
    public final <R extends Serializable, S extends Serializable> QueryWrapper<T> selectFunc(String functionName,
            ObjectFunction<T, S>... func) {
        List<WrapperValue> values = Arrays.stream(func).map(Wrapper::col).collect(Collectors.toList());
        return selectFunc(Function.CUSTOMIZE, functionName, values);
    }

    public final QueryWrapper<T> selectFunc(String functionName, WrapperValue... values) {
        return selectFunc(Function.CUSTOMIZE, functionName, Arrays.asList(values));
    }

    public final QueryWrapper<T> selectFunc(Function function, String functionName, List<WrapperValue> values) {
        SelectWrapper selectWrapper = new SelectWrapper();
        selectWrapper.setSelectType(SelectType.FUNCTION);
        selectWrapper.setFunction(function);
        selectWrapper.setFunctionName(functionName);
        selectWrapper.getValueList().addAll(values);
        selects.add(selectWrapper);
        return this;
    }

    public QueryWrapper<T> distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable, J extends Serializable> JoinWrapper leftJoin(
            Class<S> joinClass,
            ObjectFunction<J, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.LEFT, Wrapper.col(func1), Wrapper.col(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable, J extends Serializable> JoinWrapper rightJoin(
            Class<S> joinClass,
            ObjectFunction<J, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.RIGHT, Wrapper.col(func1), Wrapper.col(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable, J extends Serializable> JoinWrapper innerJoin(
            Class<S> joinClass,
            ObjectFunction<J, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.INNER, Wrapper.col(func1), Wrapper.col(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable, J extends Serializable> JoinWrapper outerJoin(
            Class<S> joinClass,
            ObjectFunction<J, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.OUTER, Wrapper.col(func1), Wrapper.col(func2));
    }


    public <S extends Serializable> JoinWrapper join(Class<S> joinClass, JoinType type,
            WrapperValue wrapperValue1, WrapperValue wrapperValue2) {
        JoinWrapper joinWrapper = new JoinWrapper(joinClass);
        joinWrapper.setJoinType(type);
        joinWrapper.add(WrapperType.EQ, null, null, wrapperValue1, wrapperValue2);
        joinWrappers.add(joinWrapper);
        return joinWrapper;
    }

    public <S extends Serializable> JoinWrapper join(Class<S> joinClass, JoinType type) {
        JoinWrapper joinWrapper = new JoinWrapper(joinClass);
        joinWrapper.setJoinType(type);
        joinWrappers.add(joinWrapper);
        return joinWrapper;
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> groupBy(ObjectFunction<S, R> func) {
        return groupBy(Wrapper.col(func));
    }

    public QueryWrapper<T> groupBy(WrapperValue wrapperValue) {
        OrderByWrapper wrapper = new OrderByWrapper();
        wrapper.getValueList().add(wrapperValue);
        groupBys.add(wrapper);
        return this;
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> desc(ObjectFunction<S, R> func) {
        return orderBy(Wrapper.col(func), OrderByType.DESC);
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> asc(ObjectFunction<S, R> func) {
        return orderBy(Wrapper.col(func), OrderByType.ASC);
    }

    public QueryWrapper<T> orderBy(WrapperValue wrapperValue, OrderByType type) {
        OrderByWrapper wrapper = new OrderByWrapper();
        wrapper.setOrderByType(type);
        wrapper.getValueList().add(wrapperValue);
        orderBys.add(wrapper);
        return this;
    }
}
