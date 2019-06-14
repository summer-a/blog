package com.hjb.blog.entity.enums;

/**
 * 通知状态
 * @author 胡江斌
 * @version 1.0
 * @title: NoticeStatus
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 22:13
 */
public enum NoticeStatus {

    NORMAL(1, "显示"),
    HIDDEN(0, "隐藏");

    private Integer value;

    private String message;

    NoticeStatus(Integer value, String message) {
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

    @Override
    public String toString() {
        return "NoticeStatus{" +
                "value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
