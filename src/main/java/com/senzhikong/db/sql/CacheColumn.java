package com.senzhikong.db.sql;

import lombok.Data;

/**
 * 数据库表列结构缓存
 * @author shu
 */
@Data
public class CacheColumn {
    /**
     * java类字段名
     */
    private String fieldName;
    /**
     * 数据库字段名
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段说明
     */
    private String comment;
    /**
     * java对应类型
     */
    private Class<?> fieldClass;
    /**
     * 表格信息
     */
    private CacheTable table;
}
