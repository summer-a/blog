package com.hjb.blog.service.normal;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.service.base.BaseService;

/**
 * 标签服务
 * @author 胡江斌
 * @version 1.0
 * @title: CommentService
 * @projectName blog
 * @description: TODO
 * @date 2019/6/14 22:23
 */

public interface TagService extends BaseService<Tag> {

    /**
     * 根据标签id查询文章列表
     * @param pageNum 起始页
     * @param pageSize 每页数量
     * @param tagId 标签id
     * @return
     */
    PageInfo<Article> pageArticleByTagId(int pageNum, int pageSize, int tagId);
}
