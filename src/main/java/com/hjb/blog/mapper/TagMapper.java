package com.hjb.blog.mapper;

import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Tag;
import tk.mapper.MyMapper;

import java.util.List;

/**
 * 标签mapper
 * @author h1525
 */
public interface TagMapper extends MyMapper<Tag> {

    List<Article> selectArticleByTagId(Integer tagId);

    /**
     * 查询所有标签，带文章数量
     *
     * @return
     */
    List<Tag> selectTagsAndArticleCount();
}