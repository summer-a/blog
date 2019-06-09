package com.hjb.blog.entity.normal;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class Page {
    @Id
    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "page_key")
    private String pageKey;

    @Column(name = "page_title")
    private String pageTitle;

    @Column(name = "page_create_time")
    private Date pageCreateTime;

    @Column(name = "page_update_time")
    private Date pageUpdateTime;

    @Column(name = "page_view_count")
    private Integer pageViewCount;

    @Column(name = "page_comment_count")
    private Integer pageCommentCount;

    @Column(name = "page_status")
    private Integer pageStatus;

    @Column(name = "page_content")
    private String pageContent;

    /**
     * @return page_id
     */
    public Integer getPageId() {
        return pageId;
    }

    /**
     * @param pageId
     */
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    /**
     * @return page_key
     */
    public String getPageKey() {
        return pageKey;
    }

    /**
     * @param pageKey
     */
    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    /**
     * @return page_title
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @param pageTitle
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * @return page_create_time
     */
    public Date getPageCreateTime() {
        return pageCreateTime;
    }

    /**
     * @param pageCreateTime
     */
    public void setPageCreateTime(Date pageCreateTime) {
        this.pageCreateTime = pageCreateTime;
    }

    /**
     * @return page_update_time
     */
    public Date getPageUpdateTime() {
        return pageUpdateTime;
    }

    /**
     * @param pageUpdateTime
     */
    public void setPageUpdateTime(Date pageUpdateTime) {
        this.pageUpdateTime = pageUpdateTime;
    }

    /**
     * @return page_view_count
     */
    public Integer getPageViewCount() {
        return pageViewCount;
    }

    /**
     * @param pageViewCount
     */
    public void setPageViewCount(Integer pageViewCount) {
        this.pageViewCount = pageViewCount;
    }

    /**
     * @return page_comment_count
     */
    public Integer getPageCommentCount() {
        return pageCommentCount;
    }

    /**
     * @param pageCommentCount
     */
    public void setPageCommentCount(Integer pageCommentCount) {
        this.pageCommentCount = pageCommentCount;
    }

    /**
     * @return page_status
     */
    public Integer getPageStatus() {
        return pageStatus;
    }

    /**
     * @param pageStatus
     */
    public void setPageStatus(Integer pageStatus) {
        this.pageStatus = pageStatus;
    }

    /**
     * @return page_content
     */
    public String getPageContent() {
        return pageContent;
    }

    /**
     * @param pageContent
     */
    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }
}