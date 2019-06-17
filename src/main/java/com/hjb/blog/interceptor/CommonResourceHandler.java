package com.hjb.blog.interceptor;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Menu;
import com.hjb.blog.entity.normal.Music;
import com.hjb.blog.entity.normal.Options;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.MenuService;
import com.hjb.blog.service.normal.MusicService;
import com.hjb.blog.service.normal.OptionsService;
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

    @Autowired
    private MusicService musicService;

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

        // 歌曲列表
        Music music = new Music();
        music.setStatus(true);
        List<Music> musics = musicService.select(music);
        session.setAttribute("musics", musics);

        return true;
    }
}
