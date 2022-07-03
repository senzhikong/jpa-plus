package com.senzhikong.db.sql.wrapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shu.Zhou
 */
public interface IWrapperService {

    /**
     * 查询所有符合条件记录
     *
     * @param queryWrapper 查询构造器
     * @param <T>          泛型
     * @return 记录列表
     */
    <T extends Serializable> List<T> findAll(QueryWrapper<T> queryWrapper);

    /**
     * 查询一条记录
     *
     * @param queryWrapper 查询构造器
     * @param <T>          泛型
     * @return 记录
     */
    <T extends Serializable> T findOne(QueryWrapper<T> queryWrapper);

    /**
     * 统计符合条件记录数量
     *
     * @param queryWrapper 查询构造器
     * @param <T>          泛型
     * @return 数量
     */
    <T extends Serializable> Long count(QueryWrapper<T> queryWrapper);

    /**
     * 分页查询
     *
     * @param queryWrapper 查询构造器
     * @param <T>          泛型
     * @return 分页查询结果
     */
    <T extends Serializable> PagerQueryWrapper<T> findByPage(PagerQueryWrapper<T> queryWrapper);
}
