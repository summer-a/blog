package com.hjb.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.User;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.service.normal.UserService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
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
    public String loginVerify(
            HttpServletRequest request,
            HttpServletResponse response,
            String username,
            String password,
            String rememberme) {
        Map<String, Object> map = new HashMap<>(2);

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName", username).orEqualTo("userEmail");
        List<User> userList = userService.selectByExample(example);

        User user = CollectionUtils.isEmpty(userList) ? null : userList.get(0);

        if (user == null) {
            map.put("code", 0);
            map.put("msg", "用户名无效！");
        } else if (!user.getUserPass().equals(password)) {
            map.put("code", 0);
            map.put("msg", "密码错误！");
        } else {
            //登录成功
            map.put("code", 1);
            map.put("msg", "");
            //添加session
            request.getSession().setAttribute("user", user);
            //添加cookie
            if (rememberme != null) {
                //创建两个Cookie对象
                Cookie nameCookie = new Cookie("username", username);
                //设置Cookie的有效期为3天
                nameCookie.setMaxAge(60 * 60 * 24 * 3);
                Cookie pwdCookie = new Cookie("password", password);
                pwdCookie.setMaxAge(60 * 60 * 24 * 3);
                response.addCookie(nameCookie);
                response.addCookie(pwdCookie);
            }
            user.setUserLastLoginTime(new Date());
            user.setUserLastLoginIp(CommonUtils.getIpAddr(request));
            userService.update(user);

        }
        String result = new JSONObject(map).toString();
        return result;
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
