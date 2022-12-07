package com.senzhikong.db.sql;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 数据库表结构缓存
 *
 * @author shu
 */
@Data
public class CacheTable {
    /**
     * 数据库表面
     */
    private String name;
    /**
     * 所属catalog
     */
    private String catalog;
    /**
     * 所属库
     */
    private String schema;
    /**
     * 对应java类名
     */
    private String className;
    /**
     * Java类全名
     */
    private String fullClass;
    /**
     * 表说明
     */
    private String comment;
    /**
     * 对应java类
     */
    private Class<?> tableClass;
    /**
     * 自动列表
     */
    private Map<String, CacheColumn> columns;

    /**
     * 获取表全名称加别名
     *
     * @return 全名称加别名
     */
    public String getFullNameAs() {
        String text = name;
        if (StringUtils.isNotEmpty(schema)) {
            text = "`" + schema + "`." + text;
        }
        text += " AS " + className;
        return text;
    }

    /**
     * 获取表别名
     *
     * @return
     */
    public String getAsName() {
        return className;
    }
}
