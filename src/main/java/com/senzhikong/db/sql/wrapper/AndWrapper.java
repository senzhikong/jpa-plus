package com.senzhikong.db.sql.wrapper;

/**
 * 搜索条件--并且
 * @author shu
 */
public class AndWrapper extends ListWrapper {
    public AndWrapper(){
        this.setType(WrapperType.AND);
    }
}
