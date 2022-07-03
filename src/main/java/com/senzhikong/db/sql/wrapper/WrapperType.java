package com.senzhikong.db.sql.wrapper;

/**
 * @author Shu.zhou
 * @date 2018年9月24日下午5:00:50
 */
public enum WrapperType {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NOT_EQ,
    /**
     * 大于
     */
    GT,
    /**
     * 小于
     */
    LT,
    /**
     * 大于等于
     */
    GE,
    /**
     * 小于等于
     */
    LE,
    /**
     * 在。。。内
     */
    IN,
    /**
     * 不在。。。内
     */
    NOT_IN,
    /**
     * 以。。。结尾
     */
    START,
    /**
     * 以。。。开通
     */
    END,
    /**
     * 像
     */
    LIKE,
    /**
     * 为空
     */
    IS_NULL,
    /**
     * 不为空
     */
    NOT_NULL,
    /**
     * 正则
     */
    REGEX,
    /**
     * where语句
     */
    WHERE_SQL,
    /**
     * json包含
     */
    JSON_CONTAINS,
    /**
     * 或
     */
    OR,
    /**
     * 且
     */
    AND,
    /**
     * 方法
     */
    FUNCTION,
    /**
     * 查询
     */
    SELECT
}
