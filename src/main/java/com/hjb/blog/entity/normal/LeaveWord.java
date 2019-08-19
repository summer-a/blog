package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 留言实体
 * @author 胡江斌
 * @version 1.0
 * @title: LeaveWord
 * @projectName blog
 * @description: TODO
 * @date 2019/7/23 16:09
 */
@Data
@NoArgsConstructor
public class LeaveWord extends BaseEntity {

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "email")
    private String email;

    @Column(name = "ip")
    private String ip;

    @Column(name = "role")
    private Integer role;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "content")
    private String content;

}
