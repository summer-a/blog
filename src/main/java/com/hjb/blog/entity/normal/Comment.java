package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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


    /**
     * 获取评论的父对象
     *
     * @return comment_pid - 评论的父对象
     */
    public Integer getCommentPid() {
        return commentPid;
    }

    /**
     * 设置评论的父对象
     *
     * @param commentPid 评论的父对象
     */
    public void setCommentPid(Integer commentPid) {
        this.commentPid = commentPid;
    }

    /**
     * 获取评论的父对象名
     *
     * @return comment_pname - 评论的父对象名
     */
    public String getCommentPname() {
        return commentPname;
    }

    /**
     * 设置评论的父对象名
     *
     * @param commentPname 评论的父对象名
     */
    public void setCommentPname(String commentPname) {
        this.commentPname = commentPname;
    }

    /**
     * 获取评论的文章的id
     *
     * @return comment_article_id - 评论的文章的id
     */
    public Integer getCommentArticleId() {
        return commentArticleId;
    }

    /**
     * 设置评论的文章的id
     *
     * @param commentArticleId 评论的文章的id
     */
    public void setCommentArticleId(Integer commentArticleId) {
        this.commentArticleId = commentArticleId;
    }

    /**
     * 获取评论的作者名
     *
     * @return comment_author_name - 评论的作者名
     */
    public String getCommentAuthorName() {
        return commentAuthorName;
    }

    /**
     * 设置评论的作者名
     *
     * @param commentAuthorName 评论的作者名
     */
    public void setCommentAuthorName(String commentAuthorName) {
        this.commentAuthorName = commentAuthorName;
    }

    /**
     * 获取评论的作者邮箱
     *
     * @return comment_author_email - 评论的作者邮箱
     */
    public String getCommentAuthorEmail() {
        return commentAuthorEmail;
    }

    /**
     * 设置评论的作者邮箱
     *
     * @param commentAuthorEmail 评论的作者邮箱
     */
    public void setCommentAuthorEmail(String commentAuthorEmail) {
        this.commentAuthorEmail = commentAuthorEmail;
    }

    /**
     * 获取评论的作者的url
     *
     * @return comment_author_url - 评论的作者的url
     */
    public String getCommentAuthorUrl() {
        return commentAuthorUrl;
    }

    /**
     * 设置评论的作者的url
     *
     * @param commentAuthorUrl 评论的作者的url
     */
    public void setCommentAuthorUrl(String commentAuthorUrl) {
        this.commentAuthorUrl = commentAuthorUrl;
    }

    /**
     * 获取评论的作者的头像
     *
     * @return comment_author_avatar - 评论的作者的头像
     */
    public String getCommentAuthorAvatar() {
        return commentAuthorAvatar;
    }

    /**
     * 设置评论的作者的头像
     *
     * @param commentAuthorAvatar 评论的作者的头像
     */
    public void setCommentAuthorAvatar(String commentAuthorAvatar) {
        this.commentAuthorAvatar = commentAuthorAvatar;
    }

    /**
     * 获取评论的内容
     *
     * @return comment_content - 评论的内容
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * 设置评论的内容
     *
     * @param commentContent 评论的内容
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * 获取评论代理
     *
     * @return comment_agent - 评论代理
     */
    public String getCommentAgent() {
        return commentAgent;
    }

    /**
     * 设置评论代理
     *
     * @param commentAgent 评论代理
     */
    public void setCommentAgent(String commentAgent) {
        this.commentAgent = commentAgent;
    }

    /**
     * 获取评论者ip
     *
     * @return comment_ip - 评论者ip
     */
    public String getCommentIp() {
        return commentIp;
    }

    /**
     * 设置评论者ip
     *
     * @param commentIp 评论者ip
     */
    public void setCommentIp(String commentIp) {
        this.commentIp = commentIp;
    }

    /**
     * 获取评论者角色
     *
     * @return comment_role - 评论者角色
     */
    public Integer getCommentRole() {
        return commentRole;
    }

    /**
     * 设置评论者角色
     *
     * @param commentRole 评论者角色
     */
    public void setCommentRole(Integer commentRole) {
        this.commentRole = commentRole;
    }

    public Boolean getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Boolean commentStatus) {
        this.commentStatus = commentStatus;
    }
}