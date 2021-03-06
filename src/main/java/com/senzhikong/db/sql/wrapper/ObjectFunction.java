package com.senzhikong.db.sql.wrapper;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author shu
 */
@FunctionalInterface
public interface ObjectFunction<T extends Serializable, R extends Serializable> extends Function<T, R>, Serializable {
}
