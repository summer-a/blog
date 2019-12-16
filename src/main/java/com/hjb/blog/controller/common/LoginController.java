package com.hjb.blog.controller.common;

import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.vo.ResponseVO;
import com.hjb.blog.field.CommonFields;
import com.hjb.blog.field.SessionFields;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.JvtcLoginUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 用户登录/注册控制器
 * @author 胡江斌
 * @version 1.0
 * @title: LoginController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/26 14:10
 */
@Controller
public class LoginController {

    @Resource
    private JvtcUserService jvtcUserService;

    /**
     * 九职教务处账号登录
     * @param username 用户名
     * @param encoded 加密后的数据
     * @param rememberMe 是否记住用户
     * @return
     */
    @PostMapping(value = "/jvtc/login")
    public String jvtcLogin(@RequestParam(value = "username", required = true) String username,
                            @RequestParam(value = "encoded", required = true) String encoded,
                            @RequestParam(value = "rememberMe", required = false, defaultValue = "false") boolean rememberMe,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            RedirectAttributes redirectAttributes
                            ) {
        HttpSession session = request.getSession();
        // 判断用户是否已经有缓存
        JvtcUser t = new JvtcUser();
        t.setUsername(username);
        JvtcUser jvtcUser = jvtcUserService.selectOne(t);

        if (jvtcUser == null || StringUtils.isEmpty(jvtcUser.getCookie())) {
            // 查询页面
            ResponseVO jResponse = JvtcLoginUtils.loginByUserNameAndEncode(username, encoded);
            // 有则直接获取
            // 登录失败
            if (jResponse.getCode() == HttpStatus.UNAUTHORIZED.value()) {
                redirectAttributes.addFlashAttribute(SessionFields.MESSAGE, "登录失败，请检查用户名或密码");
                return "redirect:/jvtc/page/login";
            }
            // 登录成功
            else {
                // 过期则清除缓存，判断数据库账号密码和提交的是否一致
                t.setPassword(encoded);
                JvtcUser jvtcUserOnline = jvtcUserService.selectOne(t);
                // 不一致则更新密码
                if (jvtcUserOnline == null) {
                    t.setId(jvtcUser.getId());
                    jvtcUserService.update(t);
                }
                session.setAttribute(SessionFields.JVTC_USER, jvtcUserOnline);
                // 存入cookie和session
                String jvtcUserId = UUID.randomUUID().toString();
                session.setAttribute(SessionFields.JVTC_USER_ID, jvtcUserId);
                // 过期时间7天，单位=秒
                session.setMaxInactiveInterval(CommonFields.ONE_DAY_SEC * 7);
                Cookie jvtcUserIdCookie = new Cookie(SessionFields.JVTC_USER_ID, jvtcUserId);
                jvtcUserIdCookie.setMaxAge(CommonFields.ONE_DAY_SEC * 7);
                jvtcUserIdCookie.setPath("/");
                response.addCookie(jvtcUserIdCookie);
                return "redirect:/timetable/index";
            }

        } else {
            // 判断用户名密码是否正确
            if (username.equals(jvtcUser.getUsername()) && encoded.equals(jvtcUser.getPassword())) {
                // 判断cookie是否过期
                if (!StringUtils.isEmpty(jvtcUser.getCookie())) {
                    if (!JvtcLoginUtils.isLogined(jvtcUser.getCookie())) {
                        // 过期，更新缓存
                        JvtcLoginUtils.loginByUserNameAndEncode(username, encoded);
                    }
                    session.setAttribute(SessionFields.JVTC_USER, jvtcUser);
                    // 存入cookie和session
                    String jvtcUserId = UUID.randomUUID().toString();
                    session.setAttribute(SessionFields.JVTC_USER_ID, jvtcUserId);
                    // 过期时间7天，单位=秒
                    session.setMaxInactiveInterval(CommonFields.ONE_DAY_SEC * 7);
                    Cookie jvtcUserIdCookie = new Cookie("JVTC_USER_ID", jvtcUserId);
                    jvtcUserIdCookie.setMaxAge(CommonFields.ONE_DAY_SEC * 7);
                    jvtcUserIdCookie.setPath("/");
                    response.addCookie(jvtcUserIdCookie);

                    return "redirect:/timetable/index";
                }
            } else {
                redirectAttributes.addFlashAttribute(SessionFields.MESSAGE, "账号密码有误");
            }
        }

        return "redirect:/jvtc/page/login";
    }
}
