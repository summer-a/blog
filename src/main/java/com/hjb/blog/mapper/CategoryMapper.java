package com.hjb.blog.mapper;

import com.hjb.blog.entity.normal.Category;
import tk.mapper.MyMapper;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {

    /**
     * 根据文章id查询分类
     * @param id 文章id
     * @return
     */
    List<Category> selectArticleCategorysByArticleId(Integer id);

}