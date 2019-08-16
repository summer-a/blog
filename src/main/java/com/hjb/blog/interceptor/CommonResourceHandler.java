package com.hjb.blog.interceptor;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.*;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.MenuService;
import com.hjb.blog.service.normal.MusicService;
import com.hjb.blog.service.normal.OptionsService;
import com.hjb.blog.util.JvtcLoginUtils;
import com.hjb.blog.util.TimeTableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 公用资源拦截器
 * @author 胡江斌
 * @version 1.0
 * @title: CommonResourceHandler
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 22:54
 */
public class CommonResourceHandler extends HandlerInterceptorAdapter {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OptionsService optionsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();

        // 菜单分类
        List<LayerMenuDTO<Category>> layerMenuDTOS = categoryService.selectCategoryToLayerMenu(new Category());
        session.setAttribute("categories", layerMenuDTOS);

        // 其他菜单
        Menu menu = new Menu();
        menu.setMenuStatus(true);
        List<Menu> menus = menuService.select(menu);
        session.setAttribute("menus", menus);

        // 获取网站通用信息
        Options option = new Options();
        option.setOptionStatus(1);
        Options op = optionsService.selectOne(option);
        session.setAttribute("option", op);

        // 随机图片数量
        session.setAttribute("images_quantity", 15);

        // 菜单列表获取
        String uri = request.getRequestURI();
        if (!CollectionUtils.isEmpty(menus)) {
            for (Menu m : menus) {
                if (uri.startsWith(m.getMenuUrl()) && m.getMenuPermission() != null  && m.getMenuPermission()) {
                    JvtcUser jvtc_user = JvtcLoginUtils.getJvtcUser(request);
                    // 用户不存在返回登录页
                    if (jvtc_user == null) {
                        // 原始请求链接
                        session.setAttribute("request_url", uri);
                        response.sendRedirect("/jvtc/page/login");
                        return false;
                    } else {
                        // cookie
                        String jvtcUserId = session.getAttribute("JVTC_USER_ID") + "";
                        // 获取用户cookie
                        Cookie[] cookies = request.getCookies();
                        for (Cookie cookie : cookies) {
                            String name = cookie.getName();
                            if (name.equals("JVTC_USER_ID")) {
                                if (cookie.getValue().equals(jvtcUserId)) {
                                    return true;
                                }
                            }
                        }
                        // 原始请求链接
                        // cookie不对，返回登录页
                        session.setAttribute("request_url", uri);
                        response.sendRedirect("/jvtc/page/login");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 项目销毁
     */
    @PreDestroy
    public void destroy() {
        // 关闭任务计划线程池
        TimeTableUtils.task.shutdownNow();

    }

}
