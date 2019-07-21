package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Notice extends BaseEntity {

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

}