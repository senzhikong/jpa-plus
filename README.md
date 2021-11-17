# Spring Data JPA 增强组件
在使用spring data jpa的过程中，一些复杂的查询和数据库方法都比较难实现
因此参考了mybatis-plus，写了一套jpa的增强组件。

## 目录
1. [源码下载](#source_code)
2. [maven安装](#maven)
3. [实体类](#entity)
4. [配置参数](#config)
5. [使用方式](#use)
6. [别名](#as_name)
7. [WrapperValue生成方式](#wrapper_value)
8. [案例参考](#reference)

<div id="source_code"></div>

### 源码下载

[Gitee地址：https://gitee.com/ZS-Tree/jpa-plus.git](https://gitee.com/ZS-Tree/jpa-plus.git)

[Github地址：https://github.com/senzhikong/jpa-plus.git](https://github.com/senzhikong/jpa-plus.git)

<div id="maven"></div>

### Maven 安装
```
<dependency>
    <groupId>com.senzhikong</groupId>
    <artifactId>jpa-plus</artifactId>
    <version>1.0.2</version>
</dependency>
```
<div id="entity"></div>

### 实体类
```
@Table(name = "blog_note")
public class BlogNote extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content")
    private String content;
    
    private String statusDesc;
    
    private Date createTime;
    
    private Date update_time;
}

```
对象属性不能写到父类中，否则使用lambda时回出错（BlogNote::getId）

<div id="config"></div>

### 配置参数
```
szk.db.naming-strategy=camel
#数据库映射规则，默认使用当前名，可选值camel（驼峰）和underline（下划线）
```
参照以上实体类
- 当@Table与@Column注解存在name值时，数据库映射为name属性值
- 当@Table与@Column注解存在name值不存在时
1. 未配置naming-strategy参数时，映射字段直接取属性名称，update_time->update_time   createTime->createTime
2. naming-strategy参数为camel（驼峰）时，update_time->updateTime   createTime->createTime
3. naming-strategy参数为underline（下划线）时update_time->时update_time    createTime->create_time


<div id="use"></div>

### 使用方式
```
        
        //新建查询  如果不需要分页，新建 QueryWrapper 即可
        PagerQueryWrapper<BlogNote> wrapper = PagerQueryWrapper.from(BlogNote.class);
        wrapper.eq(BlogNote::getStatus, "normal");//判断列status的值等于normal
        wrapper.eq(BlogNote::getUpdateTime, BlogNote::getCreateTime);//判断列updateTime的值等于列createTime的值
//        wrapper.eq(WrapperValue,WrapperValue);//传入两个WrapperValue对象
        //  gt  大于
        //  ge  大于等于
        //  lt  小于
        //  le  小于等于
        //  ne  大于
        //  in  不等于
        //  not_in  大于
        //  like  字符串包含，参数前后不需要加%
        //  start  字符串以？开头，参数后面不需要加%
        //  end  字符串以？结尾，参数签名不需要加%
        //  is_null  大于
        //  not_null  大于

        // 去除重复
        wrapper.distinct(true);
        // 默认查询构造QueryWrapper时传入对象的全部字段，如果不查询可将该参数设为false
        wrapper.setSelectEntity(false);
        // 自定义查询字段
        wrapper.select("id").as("自定义字段名");
        wrapper.select(BlogNote::getId).as("bid");
//        wrapper.select(WrapperValue) 传入一个WrapperValue对象

        //Or查询和And查询
        //Or查询
        OrWrapper orWrapper = wrapper.or();
        orWrapper.eq(BlogNote::getStatus, "normal");//判断列status的值等于normal
        orWrapper.eq(BlogNote::getUpdateTime, BlogNote::getCreateTime);//判断列updateTime的值等于列createTime的值

        //And查询
        AndWrapper andWrapper = wrapper.and();
        orWrapper.eq(BlogNote::getStatus, "normal");//判断列status的值等于normal
        orWrapper.eq(BlogNote::getUpdateTime, BlogNote::getCreateTime);//判断列updateTime的值等于列createTime的值
        //or中添加And查询，and中添加or查询
        orWrapper.and();
        andWrapper.or();


        //关联查询,LEFT,RIGHT,INNER,OUTER
        JoinWrapper join = wrapper.join(BlogArticle.class, JoinType.LEFT);
        join.eq(BlogArticle::getId, BlogNote::getId);//可以多个条件，没有限制

        //排序OrderByType.ASC，DESC
        wrapper.desc(BlogNote::getId);//倒序
        wrapper.asc(BlogNote::getId);//正序
//        wrapper.orderBy( WrapperValue 值,OrderByType.ASC) 传入一个WrapperValue 自定义排序

        //分页
        wrapper.setPageNumber(1);//页码，从第一页开始
        wrapper.setPageSize(10);//每页条数
        wrapper.setPage(true);//默认值是true，如果使用PagerQueryWrapper时，不需要分页，可以设置为false

        //该方法在案例参考中实现了
        wrapper = this.findByPage(wrapper);
        wrapper.getContent();//查询数据结果
        wrapper.getTotalPages();//总页数
        wrapper.getTotal();//总数据数
        List<BlogNote> data = this.findAll(wrapper);

```

<div id="as_name"></div>

### 别名
构造QueryWrapper和JoinWrapper时会为数据库表取一个别名，名称即为实体类的类名
字段的默认别名就是实体类中的参数名
例如
```
select BlogNote.createTime from blog_note as BlogNote
```

<div id="wrapper_value"></div>

### WrapperValue生成方式
```
        
        //？代表参数，数量没有限制，按照顺序填充
        Wrapper.func_text("(? + ? - ?)", Wrapper.col(BlogNote::getStatus), Wrapper.col(BlogNote::getContent),
                Wrapper.col(BlogNote::getContent)).toVal();
                
        Wrapper.func_text("CONCAT(?,?)", Wrapper.col(BlogNote::getStatus), Wrapper.col(BlogNote::getContent)).toVal();
        
        Wrapper.func_custom("CONCAT", Wrapper.col(BlogNote::getStatus), Wrapper.col(BlogNote::getContent)).toVal();
        
        //内置了部分函数，SUM, MAX, MIN, MOD,  COUNT,  COUNT_DISTINCT, AVG, NOW
        //其他的数据库函数或自定义函数可以通过func_text或func_custom方式调用
        Wrapper.func(Function.MAX, Wrapper.val(1));

        Wrapper.col("name");//可以是select是取的别名，也可以是 表的别名+字段名，例，BlogNote.createTime
        
        Wrapper.col(BlogNote::getId);//lambda表达式
        
        Wrapper.val(1);//String、数字、Date、BigDecimal等
        
```

<div id="reference"></div>

### 案例参考
组件中内置了Service，可以直接继承也可以复制到自己的service中去
```
package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Shu.zhou
 * @date 2018年9月27日下午3:37:13
 */
@Getter
@Setter
@Slf4j
public abstract class WrapperService implements IWrapperService {

    @PersistenceContext
    protected EntityManager entityManager;
    @Resource
    private WrapperBuilder wrapperBuilder;

    private static void setParams(Query query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
    }

    /**
     * 查询获得Map数组
     */
    public <T extends Serializable> List<Map<String, Object>> findAllOfMap(QueryWrapper<T> queryWrapper) {
        List<Object> params = new ArrayList<>();
        String sql = wrapperBuilder.selectByWrapper(queryWrapper, params);
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    /**
     * 查询获得实体对象
     */
    public <T extends Serializable> List<T> findAll(QueryWrapper<T> queryWrapper) {
        List<Object> params = new ArrayList<>();
        String sql = wrapperBuilder.selectByWrapper(queryWrapper, params);
        Query query = entityManager.createNativeQuery(sql, queryWrapper.getGenericsClass());
        setParams(query, params);
        List<T> list = query.getResultList();
        return list;
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
     * 查询单个实体对象
     */
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
    public <T extends Serializable> PagerQueryWrapper<T> findByPage(PagerQueryWrapper<T> queryWrapper) {
        Long total = count(queryWrapper);
        List<T> list = findAll(queryWrapper);
        queryWrapper.setContent(list);
        queryWrapper.setTotal(total);
        queryWrapper.build();
        return queryWrapper;
    }
}


```
