package com.hjb.blog.interceptor;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.service.normal.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        // 菜单分类
        List<LayerMenuDTO<Category>> layerMenuDTOS = categoryService.selectCategoryToLayerMenu(new Category());
        session.setAttribute("categories", layerMenuDTOS);

        return true;
    }
}
