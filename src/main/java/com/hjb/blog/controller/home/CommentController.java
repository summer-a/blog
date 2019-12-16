package com.hjb.blog.controller.home;

import com.hjb.blog.entity.enums.Role;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.User;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.util.CodeFilterUtils;
import com.hjb.blog.util.CommonUtils;
import com.hjb.blog.util.SpringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 评论控制器
 * @author 胡江斌
 * @version 1.0
 * @title: CommentController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/18 15:19
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleService articleService;

    /**
     * 提交评论
     * @return
     */
    @PostMapping(value = "/submit")
    @ResponseBody
    public ResultVO submitComment(Comment comment, HttpServletRequest request) {

        comment.setCreateTime(LocalDateTime.now());
        comment.setCommentIp(CommonUtils.getIpAddr(request));

        User user = SpringUtils.getCurrentUser();
        if (user != null) {
            comment.setCommentRole(Role.ADMIN.getValue());
        } else {
            comment.setCommentRole(Role.VISITOR.getValue());
        }
        comment.setCommentAuthorAvatar(CommonUtils.getGravatar(comment.getCommentAuthorEmail()));

        // 过滤尖括号
        comment.setCommentContent(CodeFilterUtils.replaceGtAndLt(comment.getCommentContent()));

        commentService.insert(comment);

        // 更新文章评论数
        articleService.updateCommentCount(comment.getCommentArticleId());

        return ResultVO.ok();
    }
}
