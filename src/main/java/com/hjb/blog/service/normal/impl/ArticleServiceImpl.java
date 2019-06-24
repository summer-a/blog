package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.mapper.*;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

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

    @Resource
    private ArticleTagRefMapper articleTagRefMapper;

    @Resource
    private ArticleCategoryRefMapper articleCategoryRefMapper;

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Override
    public PageInfo<Article> page(int pageNum, int pageSize, Article article, OrderField orderField) {
        PageHelper.startPage(pageNum, pageSize);

        Example articleExample = new Example(Article.class);
        articleExample.createCriteria().andEqualTo(article);
        if (orderField.getOrderType().equals(OrderField.OrderStatus.ASC)) {
            articleExample.orderBy(orderField.getOrderField()).asc();
        } else if (orderField.getOrderType().equals(OrderField.OrderStatus.DESC)) {
            articleExample.orderBy(orderField.getOrderField()).desc();
        }

        List<Article> articleList = articleMapper.selectByExample(articleExample);
//        List<Article> articleList = articleMapper.select(article);

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
        Article article = new Article();
        article.setId(id);
        article.setArticleStatus(ArticleStatus.PUBLISH.getValue());
        List<Article> articleList = articleMapper.selectFullArticle(article);
        if (!CollectionUtils.isEmpty(articleList)) {
            Article art = articleList.get(0);
            // 设置分类
            art.setCategoryList(categoryMapper.selectArticleCategorysByArticleId(art.getId()));

            // 设置标签
            art.setTagList(articleTagRefMapper.selectTagByArticleId(id));
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

    /**
     * 更新评论数
     *
     * @param articleId 文章id
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public boolean updateCommentCount(Integer articleId) {
        return articleMapper.updateCommentCount(articleId);
    }

    /**
     * 根据类型id分页文章
     *
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @param categoryId 分类id
     * @param status 文章状态
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public PageInfo<Article> pageArticleByCategoryId(int pageNum, int pageSize, Integer categoryId, ArticleStatus status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectArticleByCategoryId(categoryId, status.getValue());
        PageInfo<Article> articlePageInfo = new PageInfo<>(articleList);

        return articlePageInfo;
    }
}
