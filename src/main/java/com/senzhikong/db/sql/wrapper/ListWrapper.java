package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author shu
 */
@Getter
@Setter
public class ListWrapper extends Wrapper {
    protected List<Wrapper> wrapperList = new ArrayList<>();

    protected ListWrapper add(WrapperType type, Function function, String functionName,
            WrapperValue... values) {
        List<WrapperValue> valueList = Arrays.asList(values);
        Wrapper wrapper = new Wrapper();
        wrapper.setType(type);
        wrapper.setFunction(function);
        wrapper.setFunctionName(functionName);
        wrapper.setValueList(valueList);
        this.wrapperList.add(wrapper);
        return this;
    }


    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper eq(
            ObjectFunction<S, R> func1,
            ObjectFunction<U, V> func2) {
        return add(WrapperType.EQ, null, null, Wrapper.col(func1), Wrapper.col(func2));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper eq(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.EQ, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public ListWrapper eq(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.EQ, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper ne(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.NOT_EQ, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper ne(
            ObjectFunction<S, R> func1, ObjectFunction<U, V> func2) {
        return add(WrapperType.NOT_EQ, null, null, Wrapper.col(func1), Wrapper.val(func2));
    }

    public ListWrapper ne(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.NOT_EQ, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper gt(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.GT, null, null, Wrapper.col(func1), Wrapper.val(value));
    }


    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper gt(
            ObjectFunction<S, R> func1, ObjectFunction<U, V> func2) {
        return add(WrapperType.GT, null, null, Wrapper.col(func1), Wrapper.val(func2));
    }

    public ListWrapper gt(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.GT, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper lt(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.LT, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper lt(
            ObjectFunction<S, R> func1, ObjectFunction<U, V> func2) {
        return add(WrapperType.LT, null, null, Wrapper.col(func1), Wrapper.val(func2));
    }

    public ListWrapper lt(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.LT, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper ge(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.GE, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper ge(
            ObjectFunction<S, R> func1, ObjectFunction<U, V> func2) {
        return add(WrapperType.GE, null, null, Wrapper.col(func1), Wrapper.val(func2));
    }

    public ListWrapper ge(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.GE, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper le(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.LE, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable, U extends Serializable, V extends Serializable> ListWrapper le(
            ObjectFunction<S, R> func1, ObjectFunction<U, V> func2) {
        return add(WrapperType.LE, null, null, Wrapper.col(func1), Wrapper.val(func2));
    }

    public ListWrapper le(WrapperValue val1, WrapperValue val2) {
        return add(WrapperType.LE, null, null, val1, val2);
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> ListWrapper in(
            ObjectFunction<S, R> func1,
            Iterable<K> value) {
        return add(WrapperType.IN, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper in(ObjectFunction<S, R> func1,
            Object[] value) {
        return add(WrapperType.IN, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> ListWrapper notIn(
            ObjectFunction<S, R> func1,
            Collection<K> value) {
        return add(WrapperType.NOT_IN, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper notIn(ObjectFunction<S, R> func1,
            Object[] value) {
        return add(WrapperType.NOT_IN, null, null, Wrapper.col(func1), Wrapper.val(value));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper start(ObjectFunction<S, R> func1,
            String value) {
        return add(WrapperType.LIKE, null, null, Wrapper.col(func1), Wrapper.val(value + "%"));
    }

    public ListWrapper start(
            WrapperValue val, String value) {
        return add(WrapperType.LIKE, null, null, val, Wrapper.val(value + "%"));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper end(ObjectFunction<S, R> func1,
            String value) {
        return add(WrapperType.LIKE, null, null, Wrapper.col(func1), Wrapper.val("%" + value));
    }

    public ListWrapper end(
            WrapperValue val, String value) {
        return add(WrapperType.LIKE, null, null, val, Wrapper.val("%" + value));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper like(ObjectFunction<S, R> func1,
            String value) {
        return add(WrapperType.LIKE, null, null, Wrapper.col(func1),
                Wrapper.val("%" + value + "%"));
    }

    public ListWrapper like(
            WrapperValue val, String value) {
        return add(WrapperType.LIKE, null, null, val, Wrapper.val("%" + value + "%"));
    }

    public <S extends Serializable, R extends Serializable> ListWrapper isNull(ObjectFunction<S, R> func1) {
        return add(WrapperType.IS_NULL, null, null, Wrapper.col(func1));
    }

    public ListWrapper isNull(
            WrapperValue val) {
        return add(WrapperType.IS_NULL, null, null, val);
    }

    public <S extends Serializable, R extends Serializable> ListWrapper notNull(ObjectFunction<S, R> func1) {
        return add(WrapperType.NOT_NULL, null, null, Wrapper.col(func1));
    }

    public ListWrapper notNull(
            WrapperValue val) {
        return add(WrapperType.NOT_NULL, null, null, val);
    }

    public OrWrapper or() {
        OrWrapper or = new OrWrapper();
        this.wrapperList.add(or);
        return or;
    }

    public <S extends Serializable, R extends Serializable> ListWrapper jsonArrayContains(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.FUNCTION, Function.CUSTOMIZE, "JSON_CONTAINS", Wrapper.col(func1),
                Wrapper.val(value.toString()));
    }

    public AndWrapper and() {
        AndWrapper and = new AndWrapper();
        this.wrapperList.add(and);
        return and;
    }

    public ListWrapper or(ListWrapper listWrapper) {
        listWrapper.setType(WrapperType.OR);
        this.wrapperList.add(listWrapper);
        return this;
    }

    public ListWrapper and(ListWrapper listWrapper) {
        listWrapper.setType(WrapperType.AND);
        this.wrapperList.add(listWrapper);
        return this;
    }


}
