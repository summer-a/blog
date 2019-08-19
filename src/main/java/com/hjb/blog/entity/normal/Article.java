package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
public class Article extends BaseEntity {

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
     * 文章内容放在另一张表
     */
    @Transient
    private String articleContent;

    @Transient
    private User user;

    @Transient
    private List<Tag> tagList;

    @Transient
    private List<Category> categoryList;

    public Article(Integer id) {
        super(id);
    }

}