package com.hjb.blog.entity.timetable;

import com.hjb.blog.entity.enums.QQType;
import lombok.Data;

/**
 * 消息实体
 * @author 胡江斌
 * @version 1.0
 * @title: MessageInfo
 * @projectName blog
 * @description: TODO
 * @date 2019/7/19 15:14
 */
@Data
public class MessageInfo {

    private Long num;

    private QQType type;

    private String message;

}
