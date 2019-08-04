package com.hjb.blog.controller.common;

import com.xiaoleilu.hutool.captcha.CaptchaUtil;
import com.xiaoleilu.hutool.captcha.LineCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
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
    @RequestMapping(value = "/imgcode.png", method = RequestMethod.GET)
    public void createImgCode(HttpSession session, HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100, 4, 150);
            String code = lineCaptcha.getCode();
            session.setAttribute("imgcode", code);
            session.setMaxInactiveInterval(60);
            lineCaptcha.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
