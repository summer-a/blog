package com.hjb.blog.service.normal;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.service.base.BaseService;

import java.util.List;

/**
 * 文章服务
 * @author 胡江斌
 * @version 1.0
 * @title: ArticleService
 * @projectName blog
 * @description: TODO
 * @date 2019/6/11 12:39
 */
public interface ArticleService extends BaseService<Article> {

    /**
     * 查询一个完整的文章信息
     * @return
     */
    Article selectOneForFullArticle(Article article);

    /**
     * 查询完整的文章信息列表
     * @return
     */
    List<Article> selectListForFullArticle(Article article);

    /**
     * 更新评论数
     * @param articleId 文章id
     * @return
     */
    boolean updateCommentCount(Integer articleId);

    /**
     * 根据标题搜索文章列表
     *
     * @param title
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Article> selectLikeArticlesByTitle(String title, int pageNum, int pageSize);

    /**
     * 根据类型id分页文章
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param categoryId 分类id
     * @param status 文章状态
     * @return
     */
    PageInfo<Article> pageArticleByCategoryId(int pageNum, int pageSize, Integer categoryId, ArticleStatus status);
}
