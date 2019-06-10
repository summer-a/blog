package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Options extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 获取简介标题
     *
     * @return option_site_title - 简介标题
     */
    public String getOptionSiteTitle() {
        return optionSiteTitle;
    }

    /**
     * 设置简介标题
     *
     * @param optionSiteTitle 简介标题
     */
    public void setOptionSiteTitle(String optionSiteTitle) {
        this.optionSiteTitle = optionSiteTitle;
    }

    /**
     * 获取站点描述
     *
     * @return option_site_descrption - 站点描述
     */
    public String getOptionSiteDescrption() {
        return optionSiteDescrption;
    }

    /**
     * 设置站点描述
     *
     * @param optionSiteDescrption 站点描述
     */
    public void setOptionSiteDescrption(String optionSiteDescrption) {
        this.optionSiteDescrption = optionSiteDescrption;
    }

    /**
     * 获取标签描述
     *
     * @return option_meta_descrption - 标签描述
     */
    public String getOptionMetaDescrption() {
        return optionMetaDescrption;
    }

    /**
     * 设置标签描述
     *
     * @param optionMetaDescrption 标签描述
     */
    public void setOptionMetaDescrption(String optionMetaDescrption) {
        this.optionMetaDescrption = optionMetaDescrption;
    }

    /**
     * 获取标签关键字
     *
     * @return option_meta_keyword - 标签关键字
     */
    public String getOptionMetaKeyword() {
        return optionMetaKeyword;
    }

    /**
     * 设置标签关键字
     *
     * @param optionMetaKeyword 标签关键字
     */
    public void setOptionMetaKeyword(String optionMetaKeyword) {
        this.optionMetaKeyword = optionMetaKeyword;
    }

    /**
     * 获取关于本站-头像
     *
     * @return option_aboutsite_avatar - 关于本站-头像
     */
    public String getOptionAboutsiteAvatar() {
        return optionAboutsiteAvatar;
    }

    /**
     * 设置关于本站-头像
     *
     * @param optionAboutsiteAvatar 关于本站-头像
     */
    public void setOptionAboutsiteAvatar(String optionAboutsiteAvatar) {
        this.optionAboutsiteAvatar = optionAboutsiteAvatar;
    }

    /**
     * 获取关于本站-标题
     *
     * @return option_aboutsite_title - 关于本站-标题
     */
    public String getOptionAboutsiteTitle() {
        return optionAboutsiteTitle;
    }

    /**
     * 设置关于本站-标题
     *
     * @param optionAboutsiteTitle 关于本站-标题
     */
    public void setOptionAboutsiteTitle(String optionAboutsiteTitle) {
        this.optionAboutsiteTitle = optionAboutsiteTitle;
    }

    /**
     * 获取关于本站-内容
     *
     * @return option_aboutsite_content - 关于本站-内容
     */
    public String getOptionAboutsiteContent() {
        return optionAboutsiteContent;
    }

    /**
     * 设置关于本站-内容
     *
     * @param optionAboutsiteContent 关于本站-内容
     */
    public void setOptionAboutsiteContent(String optionAboutsiteContent) {
        this.optionAboutsiteContent = optionAboutsiteContent;
    }

    /**
     * 获取关于本站-微信
     *
     * @return option_aboutsite_wechat - 关于本站-微信
     */
    public String getOptionAboutsiteWechat() {
        return optionAboutsiteWechat;
    }

    /**
     * 设置关于本站-微信
     *
     * @param optionAboutsiteWechat 关于本站-微信
     */
    public void setOptionAboutsiteWechat(String optionAboutsiteWechat) {
        this.optionAboutsiteWechat = optionAboutsiteWechat;
    }

    /**
     * 获取关于本站-qq
     *
     * @return option_aboutsite_qq - 关于本站-qq
     */
    public String getOptionAboutsiteQq() {
        return optionAboutsiteQq;
    }

    /**
     * 设置关于本站-qq
     *
     * @param optionAboutsiteQq 关于本站-qq
     */
    public void setOptionAboutsiteQq(String optionAboutsiteQq) {
        this.optionAboutsiteQq = optionAboutsiteQq;
    }

    /**
     * 获取关于本站-github
     *
     * @return option_aboutsite_github - 关于本站-github
     */
    public String getOptionAboutsiteGithub() {
        return optionAboutsiteGithub;
    }

    /**
     * 设置关于本站-github
     *
     * @param optionAboutsiteGithub 关于本站-github
     */
    public void setOptionAboutsiteGithub(String optionAboutsiteGithub) {
        this.optionAboutsiteGithub = optionAboutsiteGithub;
    }

    /**
     * 获取关于本站-微博
     *
     * @return option_aboutsite_weibo - 关于本站-微博
     */
    public String getOptionAboutsiteWeibo() {
        return optionAboutsiteWeibo;
    }

    /**
     * 设置关于本站-微博
     *
     * @param optionAboutsiteWeibo 关于本站-微博
     */
    public void setOptionAboutsiteWeibo(String optionAboutsiteWeibo) {
        this.optionAboutsiteWeibo = optionAboutsiteWeibo;
    }

    /**
     * 获取统计
     *
     * @return option_tongji - 统计
     */
    public String getOptionTongji() {
        return optionTongji;
    }

    /**
     * 设置统计
     *
     * @param optionTongji 统计
     */
    public void setOptionTongji(String optionTongji) {
        this.optionTongji = optionTongji;
    }

    /**
     * 获取状态
     *
     * @return option_status - 状态
     */
    public Integer getOptionStatus() {
        return optionStatus;
    }

    /**
     * 设置状态
     *
     * @param optionStatus 状态
     */
    public void setOptionStatus(Integer optionStatus) {
        this.optionStatus = optionStatus;
    }

}