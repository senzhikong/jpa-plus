package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询指定字段
 * @author shu
 */
@Getter
@Setter
public class SelectWrapper extends Wrapper {
    private String asName;

    public void as(String asName) {
        this.asName = asName;
    }
}
