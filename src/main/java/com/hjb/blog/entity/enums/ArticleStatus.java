package com.hjb.blog.entity.enums;

/**
 * 文章状态
 * @author 胡江斌
 * @version 1.0
 * @title: ArticleStatus
 * @projectName blog
 * @description: TODO
 * @date 2019/6/11 11:58
 */
public enum ArticleStatus {

    PUBLISH(1, "已发布"),
    DRAFT(0, "草稿");

    private Integer value;

    private String message;

    ArticleStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
