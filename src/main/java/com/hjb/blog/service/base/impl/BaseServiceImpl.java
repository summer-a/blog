package com.hjb.blog.service.base.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.base.BaseEntity;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mapper.MyMapper;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: BaseServiceImpl
 * @projectName blog
 * @description: TODO 基本Mapper
 * @date 2019/6/8 10:42
 */
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private MyMapper<T> mapper;

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insert(T t) {
        t.setCreateTime(LocalDateTime.now());
        t.setUpdateTime(LocalDateTime.now());
        return mapper.insert(t);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insertSelective(T t) {
        t.setCreateTime(LocalDateTime.now());
        t.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(t);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int delete(T t) {
        return mapper.delete(t);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int update(T t) {
        t.setUpdateTime(LocalDateTime.now());
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
    public PageInfo<T> page(int pageNum, int pageSize, T t, OrderField orderField) {
        PageHelper.startPage(pageNum, pageSize);

        Example example = new Example(t.getClass());
        if (orderField.getOrderType().equals(OrderField.OrderStatus.ASC)) {
            example.orderBy(orderField.getOrderField()).asc();
        } else if (orderField.getOrderType().equals(OrderField.OrderStatus.DESC)) {
            example.orderBy(orderField.getOrderField()).desc();
        }

        example.createCriteria().andEqualTo(t);
        List<T> list = mapper.selectByExample(example);
//        List<T> list = mapper.select(t);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int selectCount(T t) {
        return mapper.selectCount(t);
    }
}
