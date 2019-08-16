package com.hjb.blog.util;

import com.hjb.blog.entity.normal.User;

import javax.servlet.http.HttpSession;

/**
 * 后台用户工具
 *
 * @author 胡江斌
 * @version 1.0
 * @title: AdminUserUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/8/15 14:30
 */
public class AdminUserUtils {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static User getCurrentUser() {
        HttpSession currentSession = SpringUtils.getCurrentSession();
        return (User) currentSession.getAttribute("user");
    }
}
