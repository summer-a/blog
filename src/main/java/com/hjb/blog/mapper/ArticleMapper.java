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

}