package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文章对应用户id
     */
    @Column(name = "article_user_id")
    private Integer articleUserId;

    /**
     * 文章标题
     */
    @Column(name = "article_title")
    private String articleTitle;

    /**
     * 文章被查看数
     */
    @Column(name = "article_view_count")
    private Integer articleViewCount;

    /**
     * 文章被评论数
     */
    @Column(name = "article_comment_count")
    private Integer articleCommentCount;

    /**
     * 文章被赞数
     */
    @Column(name = "article_like_count")
    private Integer articleLikeCount;

    /**
     * 文章是否允许评论
     */
    @Column(name = "article_is_comment")
    private Integer articleIsComment;

    /**
     * 文章状态
     */
    @Column(name = "article_status")
    private Integer articleStatus;

    /**
     * 文章排序
     */
    @Column(name = "article_order")
    private Integer articleOrder;

    /**
     * 文章摘要
     */
    @Column(name = "article_summary")
    private String articleSummary;

    /**
     * 文章内容
     */
    @Column(name = "article_content")
    private String articleContent;

    @Transient
    private User user;

    @Transient
    private List<Tag> tagList;

    @Transient
    private List<Category> categoryList;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取文章对应用户id
     *
     * @return article_user_id - 文章对应用户id
     */
    public Integer getArticleUserId() {
        return articleUserId;
    }

    /**
     * 设置文章对应用户id
     *
     * @param articleUserId 文章对应用户id
     */
    public void setArticleUserId(Integer articleUserId) {
        this.articleUserId = articleUserId;
    }

    /**
     * 获取文章标题
     *
     * @return article_title - 文章标题
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * 设置文章标题
     *
     * @param articleTitle 文章标题
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * 获取文章被查看数
     *
     * @return article_view_count - 文章被查看数
     */
    public Integer getArticleViewCount() {
        return articleViewCount;
    }

    /**
     * 设置文章被查看数
     *
     * @param articleViewCount 文章被查看数
     */
    public void setArticleViewCount(Integer articleViewCount) {
        this.articleViewCount = articleViewCount;
    }

    /**
     * 获取文章被评论数
     *
     * @return article_comment_count - 文章被评论数
     */
    public Integer getArticleCommentCount() {
        return articleCommentCount;
    }

    /**
     * 设置文章被评论数
     *
     * @param articleCommentCount 文章被评论数
     */
    public void setArticleCommentCount(Integer articleCommentCount) {
        this.articleCommentCount = articleCommentCount;
    }

    /**
     * 获取文章被赞数
     *
     * @return article_like_count - 文章被赞数
     */
    public Integer getArticleLikeCount() {
        return articleLikeCount;
    }

    /**
     * 设置文章被赞数
     *
     * @param articleLikeCount 文章被赞数
     */
    public void setArticleLikeCount(Integer articleLikeCount) {
        this.articleLikeCount = articleLikeCount;
    }

    /**
     * 获取文章是否允许评论
     *
     * @return article_is_comment - 文章是否允许评论
     */
    public Integer getArticleIsComment() {
        return articleIsComment;
    }

    /**
     * 设置文章是否允许评论
     *
     * @param articleIsComment 文章是否允许评论
     */
    public void setArticleIsComment(Integer articleIsComment) {
        this.articleIsComment = articleIsComment;
    }

    /**
     * 获取文章状态
     *
     * @return article_status - 文章状态
     */
    public Integer getArticleStatus() {
        return articleStatus;
    }

    /**
     * 设置文章状态
     *
     * @param articleStatus 文章状态
     */
    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    /**
     * 获取文章排序
     *
     * @return article_order - 文章排序
     */
    public Integer getArticleOrder() {
        return articleOrder;
    }

    /**
     * 设置文章排序
     *
     * @param articleOrder 文章排序
     */
    public void setArticleOrder(Integer articleOrder) {
        this.articleOrder = articleOrder;
    }


    /**
     * 获取文章摘要
     *
     * @return article_summary - 文章摘要
     */
    public String getArticleSummary() {
        return articleSummary;
    }

    /**
     * 设置文章摘要
     *
     * @param articleSummary 文章摘要
     */
    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    /**
     * 获取文章内容
     *
     * @return article_content - 文章内容
     */
    public String getArticleContent() {
        return articleContent;
    }

    /**
     * 设置文章内容
     *
     * @param articleContent 文章内容
     */
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}