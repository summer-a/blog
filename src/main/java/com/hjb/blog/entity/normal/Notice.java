package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "notice")
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

}