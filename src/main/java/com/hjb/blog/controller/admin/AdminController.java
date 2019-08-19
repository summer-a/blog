package com.hjb.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.User;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.service.normal.UserService;
import com.hjb.blog.util.AdminUserUtils;
import com.hjb.blog.util.CommonUtils;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author h1525
 */
@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    /**
     * 超时时间
     */
    private static final int TIME_OUT = 7 * 24 * 60 * 60;

    /**
     * 后台首页
     *
     * @return
     */
    @RequestMapping("/admin")
    public String index(Model model) {
        //文章列表
        PageInfo<Article> articleList = articleService.page(1, 5, new Article(), OrderField.orderByDesc("createTime"));
        model.addAttribute("articleList", articleList.getList());
        //评论列表
        PageInfo<Comment> commentList = commentService.page(1, 5, new Comment(), OrderField.orderByDesc("createTime"));
        List<Comment> list = commentList.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (Comment c : list) {
                Example example = new Example(Article.class);
                example.createCriteria().andEqualTo("id", c.getCommentArticleId());
                example.selectProperties("articleTitle");
                List<Article> articleResult = articleService.selectByExample(example);
                if (!CollectionUtils.isEmpty(articleResult)) {
                    c.setCommentArticleTitle(articleResult.get(0).getArticleTitle());
                }
            }
        }
        model.addAttribute("commentList", list);
        return "Admin/index";
    }

    /**
     * 登录页面显示
     *
     * @return
     */
    @GetMapping("/login")
    public String loginPage() {
        User currentUser = AdminUserUtils.getCurrentUser();
        if (currentUser != null) {
            return "/admin";
        }
        return "Admin/login";
    }

    /**
     * 登录验证
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/loginVerify")
    @ResponseBody
    public ResultVO loginVerify(
            HttpServletRequest request,
            HttpServletResponse response,
            String username,
            String password,
            Boolean rememberMe) {

        HttpSession session = request.getSession();

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName", username).orEqualTo("userEmail");
        List<User> userList = userService.selectByExample(example);

        User user = CollectionUtils.isEmpty(userList) ? null : userList.get(0);

        if (user == null) {
            return ResultVO.build(0, "用户名无效!", null);
        } else if (!user.getUserPass().equals(password)) {
            return ResultVO.build(0, "密码错误!", null);
        } else {
            // 添加session
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(TIME_OUT);
            // 添加cookie
            if (rememberMe != null && rememberMe) {
                // 生成随机id
                String userId = UUID.randomUUID().toString();
                // 创建Cookie对象
                Cookie cookie = new Cookie("ADMIN_USER_ID", userId);
                // 创建session
                session.setAttribute("ADMIN_USER_ID", userId);
                // 设置Cookie的有效期为7天
                cookie.setMaxAge(TIME_OUT);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            User userUpdate = new User();
            userUpdate.setId(user.getId());
            userUpdate.setUserLastLoginTime(LocalDateTime.now());
            userUpdate.setUserLastLoginIp(CommonUtils.getIpAddr(request));
            userService.update(userUpdate);
            // 登录成功,返回请求url
            return ResultVO.build(1, "登录成功", session.getAttribute("request_url"));
        }
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @GetMapping(value = "/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }

}
