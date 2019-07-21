package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Tag extends BaseEntity {

    /**
     * 标签名
     */
    @Column(name = "tag_name")
    private String tagName;

    /**
     * 标签描述
     */
    @Column(name = "tag_description")
    private String tagDescription;

    /**
     * 获取标签名
     *
     * @return tag_name - 标签名
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * 设置标签名
     *
     * @param tagName 标签名
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 获取标签描述
     *
     * @return tag_description - 标签描述
     */
    public String getTagDescription() {
        return tagDescription;
    }

    /**
     * 设置标签描述
     *
     * @param tagDescription 标签描述
     */
    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

}