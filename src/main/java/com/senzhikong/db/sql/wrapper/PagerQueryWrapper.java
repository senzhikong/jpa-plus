package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shu.zhou
 */
@Getter
@Setter
public class PagerQueryWrapper<T extends Serializable> extends QueryWrapper<T> {

    private Integer pageNumber = 1;
    private Long total = 0L;
    private Integer totalPages = 0;
    private Integer pageSize = 10;
    private List<T> content;
    private boolean page = true;

    public PagerQueryWrapper(Class<T> clz) {
        super(clz);
    }

    public static <T extends Serializable> PagerQueryWrapper<T> from(Class<T> clz) {
        return new PagerQueryWrapper<>(clz);
    }


    public void build() {
        totalPages = Math.toIntExact(total / pageSize);
        if (total % pageSize > 0) {
            totalPages++;
        }
    }

}
