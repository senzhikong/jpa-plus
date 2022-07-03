package com.senzhikong.db.sql;

import lombok.Data;

/**
 * @author shu
 */
@Data
public class CacheColumn {
    private String fieldName;
    private String columnName;
    private String fieldType;
    private String comment;
    private Class<?> fieldClass;
    private CacheTable table;
}
