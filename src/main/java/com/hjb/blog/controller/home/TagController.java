package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.dto.NavVO;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签控制器
 *
 * @author 胡江斌
 * @version 1.0
 * @title: TagController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/23 20:39
 */
@Controller
@RequestMapping(value = "/tag", method = RequestMethod.GET)
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;


    /**
     * 指定标签的文章列表
     *
     * @param tagId    标签id
     * @param pageNum  开始页
     * @param pageSize 每页数量
     * @param model    model
     * @return
     */
    @RequestMapping(value = {"/{tagId}", "/{tagId}/{pageNum}", "/{tagId}/{pageNum}/{pageSize}"}, method = RequestMethod.GET)
    public String selectArticleListByTag(@PathVariable(value = "tagId", required = true) Integer tagId,
                                         @PathVariable(value = "pageNum", required = false) Integer pageNum,
                                         @PathVariable(value = "pageSize", required = false) Integer pageSize,
                                         Model model) {
        // 设置默认值
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        Tag tag = new Tag();
        tag.setId(tagId);
        Tag result = tagService.selectOne(tag);

        if (result == null) {
            return "redirect:/404";
        }
        model.addAttribute("nav", NavVO.covert(result));

        // 文章列表
        PageInfo<Article> tagPageInfo = tagService.pageArticleByTagId(pageNum, pageSize, tagId);
        model.addAttribute("pageInfo", tagPageInfo);

        // 侧边栏
        // 标签列表显示
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
