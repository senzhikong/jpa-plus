package com.senzhikong.db.sql;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WrapperConfig {
    public static final String CAMEL = "camel";
    public static final String UNDER_LINE = "underline";
    @Value("${szk.db.naming-strategy}")
    private String namingStrategy;
}
