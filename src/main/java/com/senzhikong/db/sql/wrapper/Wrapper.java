package com.senzhikong.db.sql.wrapper;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础构造器
 *
 * @author Shu.zhou
 * @date 2018年9月24日下午5:00:37
 */
@Data
public class Wrapper {
    protected Class<? extends Serializable> genericsClass;
    protected WrapperType type;
    private List<WrapperValue> valueList = new ArrayList<>();
    private Function function;
    private String functionName;

    private static Wrapper func(Function function, String functionName, WrapperValue... values) {
        Wrapper wrapper = new Wrapper();
        wrapper.setType(WrapperType.FUNCTION);
        wrapper.setFunction(function);
        wrapper.setFunctionName(functionName);
        wrapper.setValueList(Arrays.asList(values));
        return wrapper;
    }

    public static Wrapper func(Function function, WrapperValue... values) {
        return func(function, function.name(), values);
    }

    public static Wrapper funcCustom(String function, WrapperValue... values) {
        return func(Function.CUSTOMIZE, function, values);
    }

    public static Wrapper funcText(String function, WrapperValue... values) {
        return func(Function.CUSTOMIZE_TEXT, function, values);
    }

    public static <S extends Serializable, R extends Serializable> WrapperValue col(ObjectFunction<S, R> function) {
        return WrapperValue.col(function);
    }


    public static WrapperValue col(String column) {
        return WrapperValue.col(column);
    }

    public static WrapperValue val(Object value) {
        return WrapperValue.val(value);
    }

    public WrapperValue toVal() {
        return WrapperValue.from(this);
    }
}
