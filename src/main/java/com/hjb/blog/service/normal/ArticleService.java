package com.hjb.blog.service.normal;

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
    Article selectOneForFullArticle(Integer id);

    /**
     * 查询完整的文章信息列表
     * @return
     */
    List<Article> selectListForFullArticle(Article article);
}
