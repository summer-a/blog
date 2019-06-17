package com.hjb.blog.controller.home;

import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.service.normal.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 文章控制器
 * @author 胡江斌
 * @version 1.0
 * @title: ArticleController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/16 15:56
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 文章详情页
     * @param id 文章id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String articleDetailPage(@PathVariable(name = "id", required = true) Integer id,
                                    Model model) {
        Article article = articleService.selectOneForFullArticle(id);
        if (article == null) {
            return "Home/Error/404";
        }
        model.addAttribute("article", article);

        return "Home/Page/articleDetail";
    }
}
