package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * 九职教务处账号信息
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcUser
 * @projectName blog
 * @description: TODO
 * @date 2019/6/25 21:28
 */
@Data
@NoArgsConstructor
public class JvtcUser extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "truename")
    private String truename;

    @Column(name = "vip")
    private Boolean vip;

    @Column(name = "duration_end_time")
    private LocalDateTime durationEndTime;

    @Column(name = "clazz")
    private String clazz;

    @Column(name = "cookie")
    private String cookie;

}
