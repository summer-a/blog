package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.mapper.TagMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
