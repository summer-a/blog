package com.hjb.blog.service.normal.impl;

import com.hjb.blog.entity.normal.Notice;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.NoticeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: NoticeServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 22:11
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice> implements NoticeService {

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Override
    public List<Notice> select(Notice notice) {
        return super.select(notice);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Override
    public List<Notice> selectAll() {
        return super.selectAll();
    }

    @CacheEvict(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insertSelective(Notice notice) {
        return super.insertSelective(notice);
    }

    @CacheEvict(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insert(Notice notice) {
        return super.insert(notice);
    }

    /**
     * 根据条件更新
     *
     * @param notice  更新的内容
     * @param example 更新条件
     * @return
     */
    @CacheEvict(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int updateByExampleSelective(Notice notice, Example example) {
        return super.updateByExampleSelective(notice, example);
    }

    @CacheEvict(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int update(Notice notice) {
        return super.update(notice);
    }

    @CacheEvict(value = "notice", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return super.deleteByPrimaryKey(id);
    }
}
