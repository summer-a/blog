package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.dto.ResultVO;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.service.normal.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章控制器
 *
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

    @Resource
    private CommentService commentService;

    @Resource
    private TagService tagService;

    /**
     * 文章详情页
     *
     * @param id 文章id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String articleDetailPage(@PathVariable(name = "id", required = true) Integer id,
                                    Model model) {

        // 文章信息
        Article article = articleService.selectOneForFullArticle(id);
        if (article == null) {
            return "Home/Error/404";
        }
        model.addAttribute("article", article);

        // 评论信息
        Comment comment = new Comment();
        comment.setCommentArticleId(id);
        List<Comment> comments = commentService.select(comment);
        model.addAttribute("comments", comments);

        // 获取热评文章前10
        PageInfo<Article> hotComments = articleService.page(1, 10, new Article(), OrderField.orderByDesc("articleCommentCount"));
        model.addAttribute("hotCommentArticles", hotComments.getList());

        // 获取所有标签
        List<Tag> tags = tagService.selectAll();
        model.addAttribute("tags", tags);

        return "Home/Page/articleDetail";
    }

    /**
     * 点赞
     * @param articleId 文章id
     * @return
     */
    @RequestMapping(value = "/like/{articleId}", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO likeArticle(@PathVariable(required = true) Integer articleId) {

        Article article = new Article();
        article.setId(articleId);
        article.setArticleLikeCount(articleService.selectOne(article).getArticleLikeCount() + 1);
        articleService.update(article);

        return ResultVO.ok();
    }
}
