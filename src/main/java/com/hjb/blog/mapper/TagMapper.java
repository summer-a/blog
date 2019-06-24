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
}