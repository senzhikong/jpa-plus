package com.senzhikong.db.sql.wrapper;

/**
 * 关联类型
 * @author shu
 */
public enum JoinType {
    /**
     * 左连接
     */
    LEFT,
    /**
     * 右连接
     */
    RIGHT,
    /**
     * 内连接
     */
    INNER,
    /**
     * 外连接
     */
    OUTER
}
