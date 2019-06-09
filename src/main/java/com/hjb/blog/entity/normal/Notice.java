package com.hjb.blog.entity.normal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 通知标题
     */
    @Column(name = "notice_title")
    private String noticeTitle;

    /**
     * 通知内容
     */
    @Column(name = "notice_content")
    private String noticeContent;

    /**
     * 通知状态
     */
    @Column(name = "notice_status")
    private Integer noticeStatus;

    /**
     * 通知排序
     */
    @Column(name = "notice_order")
    private Integer noticeOrder;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取通知标题
     *
     * @return notice_title - 通知标题
     */
    public String getNoticeTitle() {
        return noticeTitle;
    }

    /**
     * 设置通知标题
     *
     * @param noticeTitle 通知标题
     */
    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    /**
     * 获取通知内容
     *
     * @return notice_content - 通知内容
     */
    public String getNoticeContent() {
        return noticeContent;
    }

    /**
     * 设置通知内容
     *
     * @param noticeContent 通知内容
     */
    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    /**
     * 获取通知状态
     *
     * @return notice_status - 通知状态
     */
    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * 设置通知状态
     *
     * @param noticeStatus 通知状态
     */
    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    /**
     * 获取通知排序
     *
     * @return notice_order - 通知排序
     */
    public Integer getNoticeOrder() {
        return noticeOrder;
    }

    /**
     * 设置通知排序
     *
     * @param noticeOrder 通知排序
     */
    public void setNoticeOrder(Integer noticeOrder) {
        this.noticeOrder = noticeOrder;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后更新时间
     *
     * @return update_time - 最后更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param updateTime 最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}