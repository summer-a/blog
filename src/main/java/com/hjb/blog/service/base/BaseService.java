package com.hjb.blog.service.base;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 基础Service
 * @author 胡江斌
 * @version 1.0
 * @title: BaseService
 * @projectName blog
 * @description: TODO 基础Service
 * @date 2019/6/8 10:41
 */
public interface BaseService<T> {

    /**
     * 插入数据，null值也会保存
     * @param t 实体类
     * @return
     */
    int insert(T t);

    /**
     * 插入数据，null值不会保存
     * @param t 实体类
     * @return
     */
    int insertSelective(T t);

    /**
     * 根据主键删除数据
     * @param t 实体类
     * @return
     */
    int delete(T t);

    /**
     * 根据主键id删除
     * @param id id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据主键更新属性不为null的值
     * @param t 实体类
     * @return
     */
    int update(T t);

    /**
     * 根据实体类查询
     * @param t 实体类
     * @return
     */
    List<T> select(T t);

    /**
     * 查询所有
     * @return
     */
    List<T> selectAll();

    /**
     * 查询单个数据
     * @param t 实体类
     * @return
     */
    T selectOne(T t);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T selectById(int id);

    /**
     * 根据Example查询
     * @param example 查询条件
     * @return
     */
    List<T> selectByExample(Example example);

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param t 实体类
     * @param orderBy 排序对象
     * @return
     */
    PageInfo<T> page(int pageNum, int pageSize, T t, OrderField orderBy);

    /**
     * 查询数据数量
     * @param t 实体类
     * @return
     */
    int selectCount(T t);
}
