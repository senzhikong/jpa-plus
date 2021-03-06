package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 关联查询
 * @author shu
 */
@Getter
@Setter
public class JoinWrapper extends ListWrapper {
    private JoinType joinType;

    public JoinWrapper(Class<? extends Serializable> joinClass) {
        this.genericsClass = joinClass;
    }
}
