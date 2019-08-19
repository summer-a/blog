package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class Options extends BaseEntity {

    /**
     * 简介标题
     */
    @Column(name = "option_site_title")
    private String optionSiteTitle;

    /**
     * 站点描述
     */
    @Column(name = "option_site_descrption")
    private String optionSiteDescrption;

    /**
     * 标签描述
     */
    @Column(name = "option_meta_descrption")
    private String optionMetaDescrption;

    /**
     * 标签关键字
     */
    @Column(name = "option_meta_keyword")
    private String optionMetaKeyword;

    /**
     * 关于本站-头像
     */
    @Column(name = "option_aboutsite_avatar")
    private String optionAboutsiteAvatar;

    /**
     * 关于本站-标题
     */
    @Column(name = "option_aboutsite_title")
    private String optionAboutsiteTitle;

    /**
     * 关于本站-内容
     */
    @Column(name = "option_aboutsite_content")
    private String optionAboutsiteContent;

    /**
     * 关于本站-微信
     */
    @Column(name = "option_aboutsite_wechat")
    private String optionAboutsiteWechat;

    /**
     * 关于本站-qq
     */
    @Column(name = "option_aboutsite_qq")
    private String optionAboutsiteQq;

    /**
     * 关于本站-github
     */
    @Column(name = "option_aboutsite_github")
    private String optionAboutsiteGithub;

    /**
     * 关于本站-微博
     */
    @Column(name = "option_aboutsite_weibo")
    private String optionAboutsiteWeibo;

    /**
     * 统计
     */
    @Column(name = "option_tongji")
    private String optionTongji;

    /**
     * 状态
     */
    @Column(name = "option_status")
    private Integer optionStatus;

}