package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.NoticeStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.Notice;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.service.normal.NoticeService;
import com.hjb.blog.service.normal.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: IndexController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/10 12:50
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    /**
     * 首页
     * @param model model
     * @param startPage 起始页
     * @param pageSize 每页文章数量
     * @return
     */
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int startPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize
            ) {
        // 设置条件为已发布
        Article article = new Article();
        article.setArticleStatus(ArticleStatus.PUBLISH.getValue());

        // 获取文章列表
        PageInfo<Article> articles = articleService.page(startPage, pageSize, article, OrderField.orderByDesc("updateTime"));
        model.addAttribute("articles", articles);

        // 获取通知
        Notice notice = new Notice();
        notice.setNoticeStatus(NoticeStatus.NORMAL.getValue());
        List<Notice> notices = noticeService.select(notice);
        model.addAttribute("notices", notices);

        // 获取网站统计数据
        Map<String, String> siteBasicStatistics = new HashMap<>(5);
        siteBasicStatistics.put("commentCount", commentService.selectCount(new Comment()) + "");
        model.addAttribute("siteBasicStatistics", siteBasicStatistics);

        // 获取热评文章前10
        Article t = new Article();
        t.setArticleStatus(ArticleStatus.PUBLISH.getValue());
        PageInfo<Article> hotComments = articleService.page(1, 10, t, OrderField.orderByDesc("articleCommentCount"));
        model.addAttribute("hotCommentArticles", hotComments.getList());

        //标签列表显示
        List<Tag> allTagList = tagService.selectAll();
        model.addAttribute("tags", allTagList);

        return "Home/index";
    }

}
