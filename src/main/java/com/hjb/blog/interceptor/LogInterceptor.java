package com.hjb.blog.interceptor;

import com.alibaba.fastjson.JSON;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.util.CommonUtils;
import com.hjb.blog.util.JvtcLoginUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志拦截器
 * @author 胡江斌
 * @version 1.0
 * @title: LogInterceptor
 * @projectName blog
 * @description: TODO
 * @date 2019/7/19 12:13
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    /** 日志 */
    private Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求开始时间
        request.setAttribute("request_start_time", System.currentTimeMillis());
        return true;
    }

    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 获取请求开始时间
        Long startTime = (Long) request.getAttribute("request_start_time");
        // 请求结束时间
        long endTime = System.currentTimeMillis();
        log.info("本次请求耗时：" + (endTime - startTime) + "毫秒；" + getRequestInfo(request).toString());
    }

    /**
     * 主要功能:获取请求详细信息
     * 注意事项:无
     *
     * @param request 请求
     * @return 请求信息
     */
    private StringBuilder getRequestInfo(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        StringBuilder reqInfo = new StringBuilder();
        reqInfo.append(" 请求路径=" + servletPath);
        reqInfo.append(" 来源IP=" + CommonUtils.getIpAddr(request));


        String userName = "[游客]";
        try {
            JvtcUser jvtcUser = JvtcLoginUtils.getJvtcUser(request);
            if (jvtcUser != null) {
                userName = String.format("[%s]", jvtcUser.getUsername());
            }
        } catch (Exception e) {
        }
        reqInfo.append(" 操作人=" + userName);
        Map<String, String[]> parameterMap = request.getParameterMap();
        reqInfo.append(" 请求参数=" + JSON.toJSONString(parameterMap));

        return reqInfo;
    }
}
