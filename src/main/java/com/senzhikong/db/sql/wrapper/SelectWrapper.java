package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectWrapper extends Wrapper {
    private String asName;

    public void as(String asName) {
        this.asName = asName;
    }
}
