package com.hjb.blog.controller.common;

import com.hjb.blog.field.SessionFields;
import com.xiaoleilu.hutool.captcha.CaptchaUtil;
import com.xiaoleilu.hutool.captcha.LineCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 图片验证码
 * @author 胡江斌
 * @version 1.0
 * @title: ImgCodeController
 * @projectName blog
 * @description: TODO
 * @date 2019/7/23 16:28
 */
@Controller
@RequestMapping("/img")
public class ImgCodeController {

    /**
     * 生成图片验证码
     * @param session
     * @param response
     */
    @GetMapping(value = "/imgcode.png")
    public void createImgCode(HttpSession session,
                              HttpServletResponse response,
                              @RequestParam(required = false, defaultValue = "200") Integer width,
                              @RequestParam(required = false, defaultValue = "100") Integer height
                              ) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height, 4, 150);
            String code = lineCaptcha.getCode();
            session.setAttribute(SessionFields.IMG_CODE, code);
            session.setMaxInactiveInterval(120);
            lineCaptcha.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
