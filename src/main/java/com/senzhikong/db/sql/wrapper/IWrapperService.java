package com.senzhikong.db.sql.wrapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shu.Zhou
 */
public interface IWrapperService {

    <T extends Serializable> List<T> findAll(QueryWrapper<T> queryWrapper);

    <T extends Serializable> T findOne(QueryWrapper<T>  queryWrapper);

    <T extends Serializable> Long count(QueryWrapper<T>  queryWrapper);

    <T extends Serializable> PagerQueryWrapper<T>  findByPage(PagerQueryWrapper<T>  queryWrapper);
}
