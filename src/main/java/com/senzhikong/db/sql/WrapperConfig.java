package com.senzhikong.db.sql;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 语言配置
 *
 * @author shu
 */
@Data
@Component
public class WrapperConfig {
    /**
     * 驼峰命名
     */
    public static final String CAMEL = "camel";
    /**
     * 下划线命名
     */
    public static final String UNDER_LINE = "underline";
    /**
     * 命名规则
     */
    @Value("${szk.db.naming-strategy}")
    private String namingStrategy;
}
