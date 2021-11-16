package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderByWrapper  extends Wrapper {
    private OrderByType orderByType;
}
