package com.senzhikong.db.sql.wrapper;

/**
 * 查询条件-或者
 *
 * @author shu
 */
public class OrWrapper extends ListWrapper {
    public OrWrapper() {
        this.setType(WrapperType.OR);
    }
}
