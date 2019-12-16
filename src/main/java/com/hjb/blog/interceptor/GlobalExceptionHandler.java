package com.hjb.blog.interceptor;

import com.hjb.blog.field.SessionFields;
import com.hjb.blog.util.LoggerUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 全局异常处理
 * @author 胡江斌
 * @version 1.0
 * @title: GlobalExceptionHandler
 * @projectName blog
 * @description: TODO
 * @date 2019/6/20 13:00
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 默认异常处理器
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String defaultExceptionHandler(HttpServletRequest request, Exception e) {
        LoggerUtils.getLogger().error(e.getMessage(), e);
        if (e instanceof NoHandlerFoundException) {
            return "Home/Error/404";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionFields.EXCEPTION_MSG, e);
        return "Home/Error/500";
    }
}
