package com.hjb.blog.service.base.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.base.BaseEntity;
import com.hjb.blog.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author h1525
 * @version 1.0
 * @title: BaseServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/8 10:42
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private Mapper<T> mapper;

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insert(T t) {
        /*t.setCreateTime(new Date());
        t.setUpdateTime(new Date());*/
        return mapper.insert(t);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int delete(T t) {
        return mapper.deleteByPrimaryKey(t);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int update(T t) {
        /*t.setUpdateTime(new Date());*/
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public List<T> select(T t) {
        return mapper.select(t);
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    @Override
    public PageInfo<T> page(int pageNum, int pageSize, T t) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = mapper.select(t);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int selectCount(T t) {
        return mapper.selectCount(t);
    }
}
