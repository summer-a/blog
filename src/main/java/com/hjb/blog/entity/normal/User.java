package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
    private Date userLastLoginTime;

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取用户密码
     *
     * @return user_pass - 用户密码
     */
    public String getUserPass() {
        return userPass;
    }

    /**
     * 设置用户密码
     *
     * @param userPass 用户密码
     */
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    /**
     * 获取用户昵称
     *
     * @return user_nickname - 用户昵称
     */
    public String getUserNickname() {
        return userNickname;
    }

    /**
     * 设置用户昵称
     *
     * @param userNickname 用户昵称
     */
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    /**
     * 获取用户邮箱
     *
     * @return user_email - 用户邮箱
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 设置用户邮箱
     *
     * @param userEmail 用户邮箱
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * 获取用户url
     *
     * @return user_url - 用户url
     */
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * 设置用户url
     *
     * @param userUrl 用户url
     */
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * 获取用户头像
     *
     * @return user_avatar - 用户头像
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * 设置用户头像
     *
     * @param userAvatar 用户头像
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    /**
     * 获取用户最后登录ip
     *
     * @return user_last_login_ip - 用户最后登录ip
     */
    public String getUserLastLoginIp() {
        return userLastLoginIp;
    }

    /**
     * 设置用户最后登录ip
     *
     * @param userLastLoginIp 用户最后登录ip
     */
    public void setUserLastLoginIp(String userLastLoginIp) {
        this.userLastLoginIp = userLastLoginIp;
    }

    /**
     * 获取用户状态
     *
     * @return user_status - 用户状态
     */
    public Integer getUserStatus() {
        return userStatus;
    }

    /**
     * 设置用户状态
     *
     * @param userStatus 用户状态
     */
    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 获取用户最后登录时间
     *
     * @return user_last_login_time - 用户最后登录时间
     */
    public Date getUserLastLoginTime() {
        return userLastLoginTime;
    }

    /**
     * 设置用户最后登录时间
     *
     * @param userLastLoginTime 用户最后登录时间
     */
    public void setUserLastLoginTime(Date userLastLoginTime) {
        this.userLastLoginTime = userLastLoginTime;
    }

}