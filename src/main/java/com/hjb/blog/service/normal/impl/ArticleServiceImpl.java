package com.hjb.blog.service.normal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.*;
import com.hjb.blog.mapper.*;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.ArticleService;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    private ArticleCategoryRefMapper articleCategoryRefMapper;

    @Resource
    private ArticleTagRefMapper articleTagRefMapper;

    @Resource
    private ArticleContentMapper articleContentMapper;

    @Resource
    private CommentMapper commentMapper;

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
     * @param article
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public Article selectOneForFullArticle(Article article) {
        if (article == null || article.getId() == null) {
            return null;
        }
        List<Article> articleList = articleMapper.selectFullArticle(article);
        if (!CollectionUtils.isEmpty(articleList)) {
            Article art = articleList.get(0);
            // 设置分类
            art.setCategoryList(categoryMapper.selectArticleCategorysByArticleId(art.getId()));

            // 设置标签
            art.setTagList(articleTagRefMapper.selectTagByArticleId(article.getId()));

            // 查看数+1
            articleMapper.updateViewCount(article.getId());
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
     * @param status     文章状态
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public PageInfo<Article> pageArticleByCategoryId(int pageNum, int pageSize, Integer categoryId, ArticleStatus status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.selectArticleByCategoryId(categoryId, status.getValue());
        PageInfo<Article> articlePageInfo = new PageInfo<>(articleList);

        return articlePageInfo;
    }

    /**
     * 根据主键删除
     *
     * @param id id
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public int deleteByPrimaryKey(Integer id) {
        // 删除分类引用
        ArticleCategoryRef articleCategoryRef = new ArticleCategoryRef();
        articleCategoryRef.setArticleId(id);
        articleCategoryRefMapper.delete(articleCategoryRef);
        // 删除标签引用
        ArticleTagRef articleTagRef = new ArticleTagRef();
        articleTagRef.setArticleId(id);
        articleTagRefMapper.delete(articleTagRef);
        // 删除评论
        Comment comment = new Comment();
        comment.setCommentArticleId(id);
        commentMapper.delete(comment);
        // 删除内容
        articleContentMapper.deleteByPrimaryKey(id);
        // 删除主体
        return super.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件删除
     *
     * @param article
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public int delete(Article article) {
        // 删除
        List<Article> articles = articleMapper.select(article);
        if (!CollectionUtils.isEmpty(articles)) {
            for (Article a : articles) {
                // 删除分类引用
                ArticleCategoryRef articleCategoryRef = new ArticleCategoryRef();
                articleCategoryRef.setArticleId(a.getId());
                articleCategoryRefMapper.delete(articleCategoryRef);
                // 删除标签引用
                ArticleTagRef articleTagRef = new ArticleTagRef();
                articleTagRef.setArticleId(a.getId());
                articleTagRefMapper.delete(articleTagRef);
                // 删除评论
                Comment comment = new Comment();
                comment.setCommentArticleId(a.getId());
                commentMapper.delete(comment);
                // 删除内容
                articleContentMapper.deleteByPrimaryKey(a.getId());
                // 删除主体
                super.deleteByPrimaryKey(a.getId());
            }

            return articles.size();
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public int update(Article article) {
        // 更新文章基本信息, 内容, 分类, 标签, 新增也是
        if (article != null) {
            // 更新内容
            String articleContent = article.getArticleContent();
            ArticleContent content = new ArticleContent();
            content.setArticleContent(articleContent);
            content.setUpdateTime(LocalDateTime.now());

            Example example = new Example(ArticleContent.class);
            example.createCriteria().andEqualTo("articleId", article.getId());

            articleContentMapper.updateByExampleSelective(content, example);

            // 更新分类(删除-新增)
            List<Category> categoryList = article.getCategoryList();
            if (!CollectionUtils.isEmpty(categoryList)) {
                // 删除
                ArticleCategoryRef articleCategoryRef = new ArticleCategoryRef();
                articleCategoryRef.setArticleId(article.getId());
                articleCategoryRefMapper.delete(articleCategoryRef);
                // 新增
                for (Category category : categoryList) {
                    ArticleCategoryRef articleCategoryRefAdd = new ArticleCategoryRef();
                    articleCategoryRefAdd.setCategoryId(category.getId());
                    articleCategoryRefAdd.setArticleId(article.getId());
                    articleCategoryRefAdd.setUpdateTime(LocalDateTime.now());
                    articleCategoryRefAdd.setCreateTime(LocalDateTime.now());

                    articleCategoryRefMapper.insertSelective(articleCategoryRefAdd);
                }
            }

            // 更新标签
            List<Tag> tagList = article.getTagList();

            // 删除
            ArticleTagRef articleTagRef = new ArticleTagRef();
            articleTagRef.setArticleId(article.getId());
            articleTagRefMapper.delete(articleTagRef);

            if (!CollectionUtils.isEmpty(tagList)) {
                for (Tag tag : tagList) {
                    ArticleTagRef articleTagRefAdd = new ArticleTagRef();
                    articleTagRefAdd.setTagId(tag.getId());
                    articleTagRefAdd.setArticleId(article.getId());
                    articleTagRefAdd.setUpdateTime(LocalDateTime.now());
                    articleTagRefAdd.setCreateTime(LocalDateTime.now());

                    articleTagRefMapper.insertSelective(articleTagRefAdd);
                }
            }
            // 更新摘要
            int size = 140;
            Document doc = new Html(articleContent).getDocument();
            String text = doc.text();
            int txtLength = text.length();
            String summary = text.substring(0, txtLength < 140 ? txtLength : size);
            article.setArticleSummary(summary);
            return super.update(article);
        }
        throw new NullPointerException("Article is null");
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insert(Article article) {
        // 插入主体
        int result = super.insert(article);
        // 插入内容
        ArticleContent content = new ArticleContent();
        content.setArticleContent(article.getArticleContent());
        content.setArticleId(article.getId());
        content.setCreateTime(LocalDateTime.now());
        content.setUpdateTime(LocalDateTime.now());
        articleContentMapper.insertSelective(content);
        if (result > 0) {
            // 插入分类引用
            List<Category> categoryList = article.getCategoryList();
            if (!CollectionUtils.isEmpty(categoryList)) {
                for (Category category : categoryList) {
                    ArticleCategoryRef articleCategoryRef = new ArticleCategoryRef();
                    articleCategoryRef.setArticleId(article.getId());
                    articleCategoryRef.setCategoryId(category.getId());
                    articleCategoryRef.setCreateTime(LocalDateTime.now());
                    articleCategoryRef.setUpdateTime(LocalDateTime.now());
                    articleCategoryRefMapper.insert(articleCategoryRef);
                }
            }
            // 插入标签引用
            List<Tag> tagList = article.getTagList();
            if (!CollectionUtils.isEmpty(tagList)) {
                for (Tag tag : tagList) {
                    ArticleTagRef articleTagRef = new ArticleTagRef();
                    articleTagRef.setArticleId(article.getId());
                    articleTagRef.setTagId(tag.getId());
                    articleTagRef.setCreateTime(LocalDateTime.now());
                    articleTagRef.setUpdateTime(LocalDateTime.now());
                    articleTagRefMapper.insert(articleTagRef);
                }
            }

        }
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    @Override
    public int insertSelective(Article article) {
        // 插入主体
        int result = super.insertSelective(article);
        // 插入内容
        ArticleContent content = new ArticleContent();
        content.setArticleContent(article.getArticleContent());
        content.setArticleId(article.getId());
        content.setCreateTime(LocalDateTime.now());
        content.setUpdateTime(LocalDateTime.now());
        articleContentMapper.insertSelective(content);

        if (result > 0) {
            // 插入分类引用
            List<Category> categoryList = article.getCategoryList();
            if (!CollectionUtils.isEmpty(categoryList)) {
                for (Category category : categoryList) {
                    ArticleCategoryRef articleCategoryRef = new ArticleCategoryRef();
                    articleCategoryRef.setArticleId(article.getId());
                    articleCategoryRef.setCategoryId(category.getId());
                    articleCategoryRef.setCreateTime(LocalDateTime.now());
                    articleCategoryRef.setUpdateTime(LocalDateTime.now());
                    articleCategoryRefMapper.insertSelective(articleCategoryRef);
                }
            }
            // 插入标签引用
            List<Tag> tagList = article.getTagList();
            if (!CollectionUtils.isEmpty(tagList)) {
                for (Tag tag : tagList) {
                    ArticleTagRef articleTagRef = new ArticleTagRef();
                    articleTagRef.setArticleId(article.getId());
                    articleTagRef.setTagId(tag.getId());
                    articleTagRef.setCreateTime(LocalDateTime.now());
                    articleTagRef.setUpdateTime(LocalDateTime.now());
                    articleTagRefMapper.insertSelective(articleTagRef);
                }
            }

        }
        // 更新elasticsearch搜索引擎数据
        // ..
        return result;
    }
}
