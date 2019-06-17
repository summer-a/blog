package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.mapper.ArticleContentMapper;
import com.hjb.blog.mapper.ArticleMapper;
import com.hjb.blog.mapper.CategoryMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
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

    /**
     * 查询一个完整的文章信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Override
    public Article selectOneForFullArticle(Integer id) {
        /*Article article = articleMapper.selectByPrimaryKey(id);
        ArticleContent articleContent = new ArticleContent();
        articleContent.setArticleId(id);
        List<ArticleContent> articleContents = articleContentMapper.select(articleContent);
        if (!CollectionUtils.isEmpty(articleContents)) {
            article.setArticleContent(articleContents.get(0).getArticleContent());
        }*/
        Article article = new Article();
        article.setId(id);
        article.setArticleStatus(ArticleStatus.PUBLISH.getValue());
        List<Article> articleList = articleMapper.selectFullArticle(article);
        if (!CollectionUtils.isEmpty(articleList)) {
            Article art = articleList.get(0);
            // 设置分类
            art.setCategoryList(categoryMapper.selectArticleCategorysByArticleId(art.getId()));

            // 设置标签
            // ...
            return art;
        }
        return null;
    }

    /**
     * 查询完整的文章信息列表
     *
     * @param article
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Override
    public List<Article> selectListForFullArticle(Article article) {
        List<Article> articleList = articleMapper.selectFullArticle(article);
        return articleList;
    }
}
