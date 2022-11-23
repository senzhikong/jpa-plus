package com.senzhikong.db.sql.wrapper;

/**
 * 内置方法
 * @author shu
 */

public enum Function {
    /**
     * 求和
     */
    SUM,
    /**
     * 取最大值
     */
    MAX,
    /**
     * 取最小值
     */
    MIN,
    /**
     * 求余数
     */
    MOD,
    /**
     * 统计数量
     */
    COUNT,
    /**
     * 去重后统计
     */
    COUNT_DISTINCT,
    /**
     * 平均值
     */
    AVG,
    /**
     * 当前时间
     */
    NOW,
    /**
     * 自定义方法
     */
    CUSTOMIZE,
    /**
     * 自定义文本方法
     */
    CUSTOMIZE_TEXT
}
