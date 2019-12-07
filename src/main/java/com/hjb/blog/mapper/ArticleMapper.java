package com.hjb.blog.mapper;

import com.hjb.blog.entity.normal.Article;
import org.apache.ibatis.annotations.Param;
import tk.mapper.MyMapper;

import java.util.List;

public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 查询完整文章信息
     * @param article 条件
     * @return
     */
    List<Article> selectFullArticle(@Param("article") Article article);

    /**
     * 查询单个完整的文章信息列表
     * @param  id id
     * @return
     */
    Article selectOneForFullArticle(Integer id);

    /**
     * 更新评论数
     * @param articleId 文章id
     * @return
     */
    boolean updateCommentCount(Integer articleId);

    /**
     * 更新浏览量(浏览量+1)
     * @param articleId 文章id
     * @return
     */
    boolean updateViewCount(Integer articleId);

    /**
     * 根据类型id查询文章列表
     * @param categoryId 分类id
     * @param status 状态
     * @return
     */
    List<Article> selectArticleByCategoryId(@Param("categoryId") Integer categoryId, @Param("status") Integer status);

    /**
     * 根据标题模糊查询
     *
     * @param title
     * @return
     */
    List<Article> selectLikeArticlesByTitle(String title);
}