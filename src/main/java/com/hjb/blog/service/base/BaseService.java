package com.hjb.blog.service.base;

import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author h1525
 * @version 1.0
 * @title: BaseService
 * @projectName blog
 * @description: TODO 基础Service
 * @date 2019/6/8 10:41
 */
public interface BaseService<T> {

    int insert(T t);

    int delete(T t);

    int update(T t);

    List<T> select(T t);

    List<T> selectAll();

    T selectOne(T t);

    List<T> selectByExample(Example example);

    PageInfo<T> page(int pageNum, int pageSize, T t);

    int selectCount(T t);
}
