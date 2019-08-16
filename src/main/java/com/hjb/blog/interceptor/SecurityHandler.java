package com.hjb.blog.interceptor;

import com.hjb.blog.entity.normal.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("Admin/login");
            return false;
        }
        return true;
    }
}
