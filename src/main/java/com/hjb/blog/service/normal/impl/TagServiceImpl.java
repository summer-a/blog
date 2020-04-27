package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.mapper.TagMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.TagService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签服务
 * @author 胡江斌
 * @version 1.0
 * @title: TagServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/17 19:38
 */
@Service
public class TagServiceImpl extends BaseServiceImpl<Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Override
    public List<Tag> selectAll() {
        return super.selectAll();
    }

    /**
     * 查询所有标签，带文章数量
     *
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Override
    public List<Tag> selectAllAndArticleCount() {
        return tagMapper.selectTagsAndArticleCount();
    }

    /**
     * 根据标签id查询文章列表
     *
     * @param pageNum  起始页
     * @param pageSize 每页数量
     * @param tagId    标签id
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public PageInfo<Article> pageArticleByTagId(int pageNum, int pageSize, int tagId) {

        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = tagMapper.selectArticleByTagId(tagId);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);

        return pageInfo;
    }

    @CacheEvict(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insertSelective(Tag t) {
        return super.insertSelective(t);
    }

    @CacheEvict(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insert(Tag t) {
        return super.insert(t);
    }

    /**
     * 根据条件更新
     *
     * @param tag     更新的内容
     * @param example 更新条件
     * @return
     */
    @CacheEvict(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int updateByExampleSelective(Tag tag, Example example) {
        return super.updateByExampleSelective(tag, example);
    }

    @CacheEvict(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int update(Tag tag) {
        return super.update(tag);
    }

    @CacheEvict(value = "tag", key = "#root.targetClass.getSimpleName()")
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return super.deleteByPrimaryKey(id);
    }
}
