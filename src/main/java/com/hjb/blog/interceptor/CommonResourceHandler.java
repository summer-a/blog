package com.hjb.blog.interceptor;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.*;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.MenuService;
import com.hjb.blog.service.normal.MusicService;
import com.hjb.blog.service.normal.OptionsService;
import com.hjb.blog.task.TimeTableTask;
import com.hjb.blog.util.JvtcLoginUtils;
import com.hjb.blog.util.TimeTableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private MusicService musicService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OptionsService optionsService;

    /** 报课课表页 */
    private final String TIME_TABLE = "/timetable";

    /** 刷课页 */
    private final String COURSE = "/course";

    /** 管理员页 */
    private final String ADMIN = "/admin";

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

        // 歌曲列表
        Music music = new Music();
        music.setStatus(true);
        List<Music> musics = musicService.select(music);
        session.setAttribute("musics", musics);

        // 随机图片数量
        session.setAttribute("images_quantity", 15);

        String uri = request.getRequestURI();
        // 刷课链接
        if (uri.startsWith(TIME_TABLE)) {
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
        //
        TimeTableUtils.task.shutdownNow();

    }

}
