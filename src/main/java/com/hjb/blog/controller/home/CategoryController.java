package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.entity.vo.NavVO;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类控制器
 *
 * @author 胡江斌
 * @version 1.0
 * @title: CategoryController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/20 12:51
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;

    @Resource
    private TagService tagService;

    /**
     * 分类页
     *
     * @param cateId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(value = {"/{cateId}","/{cateId}/{pageNum}","/{cateId}/{pageNum}/{pageSize}"})
    public String selectArticleListByCategory(@PathVariable(value = "cateId", required = true) Integer cateId,
                                              @PathVariable(value = "pageNum", required = false) Integer pageNum,
                                              @PathVariable(value = "pageSize", required = false) Integer pageSize,
                                           Model model) {
        // 设置默认值
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        //该分类信息
        Category c = new Category();
        c.setId(cateId);
        Category category = categoryService.selectOne(c);
        if (category == null) {
            return "redirect:Home/Error/404";
        }
        model.addAttribute("nav", NavVO.covert(category));

        //文章列表
        PageInfo<Article> articlePageInfo = articleService.pageArticleByCategoryId(pageNum, pageSize, cateId, ArticleStatus.PUBLISH);
        model.addAttribute("pageInfo", articlePageInfo);

        //侧边栏
        //标签列表显示
        List<Tag> allTagList = tagService.selectAll();
        model.addAttribute("tags", allTagList);

        //获得热评文章
        Article t = new Article();
        t.setArticleStatus(ArticleStatus.PUBLISH.getValue());
        PageInfo<Article> hotComments = articleService.page(1, 10, t, OrderField.orderByDesc("articleCommentCount"));
        model.addAttribute("hotCommentArticles", hotComments.getList());
        return "Home/Page/articleList";
    }
}
