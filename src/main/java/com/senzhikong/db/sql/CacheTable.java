package com.senzhikong.db.sql;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class CacheTable {
    private String name;
    private String catalog;
    private String schema;
    private String className;
    private String fullClass;
    private String comment;
    private Class<?> tableClass;
    private Map<String, CacheColumn> columns;

    public String getFullNameAs() {
        String text = name;
        if (StringUtils.isNotEmpty(schema)) {
            text = "`" + schema + "`." + text;
        }
        text += " AS " + className;
        return text;
    }
    public String getAsName() {
        return className;
    }
}
