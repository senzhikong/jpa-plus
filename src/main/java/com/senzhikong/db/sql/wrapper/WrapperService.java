package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础服务
 *
 * @author Shu.zhou
 * @date 2018年9月27日下午3:37:13
 */
@Getter
@Setter
public abstract class WrapperService implements IWrapperService {

    @PersistenceContext
    protected EntityManager entityManager;
    @Resource
    protected WrapperBuilder wrapperBuilder;

    protected static void setParams(Query query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
    }

    /**
     * 查询获得Map数组
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<Map<String, Object>> findAllOfMap(QueryWrapper<T> queryWrapper) {
        List<Object> params = new ArrayList<>();
        String sql = wrapperBuilder.selectByWrapper(queryWrapper, params);
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<Map<String, Object>>) query.getResultList();
    }

    /**
     * 查询获得单个Map
     */
    public <T extends Serializable> Map<String, Object> findOneOfMap(QueryWrapper<T> queryWrapper) {
        List<Map<String, Object>> list = findAllOfMap(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("查询到多条数据，但只需要一条");
        }
        return list.get(0);
    }

    /**
     * 查询获得实体对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<T> findAll(QueryWrapper<T> queryWrapper) {
        List<Object> params = new ArrayList<>();
        String sql = wrapperBuilder.selectByWrapper(queryWrapper, params);
        Query query = entityManager.createNativeQuery(sql, queryWrapper.getGenericsClass());
        setParams(query, params);
        return (List<T>) query.getResultList();
    }

    /**
     * 查询单个实体对象
     */
    @Override
    public <T extends Serializable> T findOne(QueryWrapper<T> queryWrapper) {
        List<T> list = findAll(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("查询到多条数据，但只需要一条");
        }
        return list.get(0);
    }

    /**
     * 统计数量
     */
    @Override
    public <T extends Serializable> Long count(QueryWrapper<T> queryWrapper) {
        List<Object> params = new ArrayList<>();
        String sql = wrapperBuilder.countByWrapper(queryWrapper, params);
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        return Long.parseLong(query.getSingleResult().toString());
    }

    /**
     * 分页查询获得实体对象
     */
    @Override
    public <T extends Serializable> PagerQueryWrapper<T> findByPage(PagerQueryWrapper<T> queryWrapper) {
        Long total = count(queryWrapper);
        List<T> list = findAll(queryWrapper);
        queryWrapper.setContent(list);
        queryWrapper.setTotal(total);
        queryWrapper.build();
        return queryWrapper;
    }
}
