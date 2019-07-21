package com.hjb.blog.controller.common;

import com.hjb.blog.entity.dto.JvtcResponse;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.JvtcLoginUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/jvtc/login", method = RequestMethod.POST)
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
            JvtcResponse jResponse = JvtcLoginUtils.loginByUserNameAndEncode(username, encoded);
            // 有则直接获取
            // 登录失败
            if (jResponse.getCode() == 401) {
                redirectAttributes.addFlashAttribute("message", "登录失败，请检查用户名或密码");
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
                session.setAttribute("jvtc_user", jvtcUserOnline);
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
                    session.setAttribute("jvtc_user", jvtcUser);
                    // 存入cookie和session
                    String jvtcUserId = UUID.randomUUID().toString();
                    session.setAttribute("JVTC_USER_ID", jvtcUserId);
                    // 过期时间，单位=秒
                    session.setMaxInactiveInterval(24 * 60 * 60);
                    Cookie jvtcUserIdCookie = new Cookie("JVTC_USER_ID", jvtcUserId);
                    jvtcUserIdCookie.setMaxAge(24 * 60 * 60);
                    jvtcUserIdCookie.setPath("/");
                    response.addCookie(jvtcUserIdCookie);
                    return "redirect:/timetable/index";
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "账号密码有误");
            }
        }

        return "redirect:/jvtc/page/login";
    }
}
