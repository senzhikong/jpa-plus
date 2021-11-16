package com.senzhikong.db.sql.wrapper;

/**
 * @author Shu.zhou
 * @date 2018年9月24日下午5:00:50
 */
public enum WrapperType {
    EQ,
    NOT_EQ,
    GT,
    LT,
    GE,
    LE,
    IN,
    NOT_IN,
    START,
    END,
    LIKE,
    IS_NULL,
    NOT_NULL,
    REGEX,
    WHERE_SQL,
    JSON_CONTAINS,
    OR,
    AND,
    FUNCTION,
    SELECT
}
