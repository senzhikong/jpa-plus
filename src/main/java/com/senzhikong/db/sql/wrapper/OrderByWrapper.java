package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 排序查询
 * @author shu
 */
@Getter
@Setter
public class OrderByWrapper  extends Wrapper {
    /**
     * 排序方式
     */
    private OrderByType orderByType;
}
