package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    /**
     * 评论的父对象
     */
    @Column(name = "comment_pid")
    private Integer commentPid;

    /**
     * 评论的父对象名
     */
    @Column(name = "comment_pname")
    private String commentPname;

    /**
     * 评论的文章的id
     */
    @Column(name = "comment_article_id")
    private Integer commentArticleId;

    /**
     * 评论的文章的标题
     */
    @Transient
    private String commentArticleTitle;

    /**
     * 评论的作者名
     */
    @Column(name = "comment_author_name")
    private String commentAuthorName;

    /**
     * 评论的作者邮箱
     */
    @Column(name = "comment_author_email")
    private String commentAuthorEmail;

    /**
     * 评论的作者的url
     */
    @Column(name = "comment_author_url")
    private String commentAuthorUrl;

    /**
     * 评论的作者的头像
     */
    @Column(name = "comment_author_avatar")
    private String commentAuthorAvatar;

    /**
     * 评论的内容
     */
    @Column(name = "comment_content")
    private String commentContent;

    /**
     * 评论代理
     */
    @Column(name = "comment_agent")
    private String commentAgent;

    /**
     * 评论者ip
     */
    @Column(name = "comment_ip")
    private String commentIp;

    /**
     * 评论者角色
     */
    @Column(name = "comment_role")
    private Integer commentRole;

    @Column(name = "comment_status")
    private Boolean commentStatus;

}