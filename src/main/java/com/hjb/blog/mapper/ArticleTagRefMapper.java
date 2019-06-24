package com.hjb.blog.mapper;


import com.hjb.blog.entity.normal.ArticleTagRef;
import com.hjb.blog.entity.normal.Tag;
import tk.mapper.MyMapper;

import java.util.List;

public interface ArticleTagRefMapper extends MyMapper<ArticleTagRef> {

    /**
     * 根据文章id查询该文章标签
     * @param id 文章id
     * @return
     */
    List<Tag> selectTagByArticleId(Integer id);
}