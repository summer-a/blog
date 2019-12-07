package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @Column(name = "user_pass")
    private String userPass;

    /**
     * 用户昵称
     */
    @Column(name = "user_nickname")
    private String userNickname;

    /**
     * 用户邮箱
     */
    @Column(name = "user_email")
    private String userEmail;

    /**
     * 用户url
     */
    @Column(name = "user_url")
    private String userUrl;

    /**
     * 用户头像
     */
    @Column(name = "user_avatar")
    private String userAvatar;

    /**
     * 用户最后登录ip
     */
    @Column(name = "user_last_login_ip")
    private String userLastLoginIp;

    /**
     * 用户状态
     */
    @Column(name = "user_status")
    private Integer userStatus;

    /**
     * 用户最后登录时间
     */
    @Column(name = "user_last_login_time")
    private LocalDateTime userLastLoginTime;

}