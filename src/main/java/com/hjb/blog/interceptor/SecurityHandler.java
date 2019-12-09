package com.hjb.blog.interceptor;

import com.hjb.blog.entity.normal.User;
import com.hjb.blog.util.AdminUserUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 管理页面安全处理
 * @author 胡江斌
 * @version 1.0
 * @title: SecurityHandler
 * @projectName blog
 * @description: TODO
 * @date 2019/8/16 8:32
 */
public class SecurityHandler extends HandlerInterceptorAdapter {

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = AdminUserUtils.getCurrentUser();
        if (user != null) {
            String adminUserId = session.getAttribute("ADMIN_USER_ID") + "";
            // 判断cookie和session是否对应
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Objects.equals(cookie.getName(), "ADMIN_USER_ID") && Objects.equals(cookie.getValue(), adminUserId)) {
                        return true;
                    }
                }
            }
            // 如果没有ADMIN_USER_ID的cookie和session,清除掉用户
            AdminUserUtils.logoutCurrentUser();
        }
        // 跳转请求地址有点问题, 暂时搁着
        //session.setAttribute("request_url", request.getRequestURI());
        response.sendRedirect("/login");
        return false;
    }
}
