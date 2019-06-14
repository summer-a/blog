package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.mapper.ArticleMapper;
import com.hjb.blog.mapper.CategoryMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: ArticleServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/11 12:42
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public PageInfo<Article> page(int pageNum, int pageSize, Article article) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.select(article);

        for (Article a : articleList) {
            List<Category> categories = categoryMapper.selectArticleCategorysByArticleId(a.getId());
            // 如果没有分类
            if (CollectionUtils.isEmpty(categories)) {
                categories = new ArrayList<>();
                categories.add(Category.getDefault());
            }
            a.setCategoryList(categories);
        }

        return new PageInfo<>(articleList);
    }
}
