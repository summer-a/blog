package com.hjb.blog.controller.common;

import com.hjb.blog.field.SessionFields;
import com.hjb.blog.field.UrlFields;
import com.xiaoleilu.hutool.captcha.CaptchaUtil;
import com.xiaoleilu.hutool.captcha.LineCaptcha;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * 图片验证码
 *
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
     *
     * @param session
     * @param response
     */
    @GetMapping(value = "/verifycode.png", produces = MediaType.IMAGE_PNG_VALUE)
    public void createVerifyCode(HttpSession session,
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

    private OkHttpClient client = new OkHttpClient();

    /**
     * 获取职教云专有验证码<br>
     * 抽取其中的Cookie
     */
    @GetMapping(value = "/icve/verifycode.png", produces = MediaType.IMAGE_PNG_VALUE)
    public void getIcveVerifyCode(HttpSession session, HttpServletResponse resp) {
        Request request = new Request.Builder().url(UrlFields.ICVE_VERIFY_CODE + "?t=" + Math.random()).build();
        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful()) {
                List<String> cookies = response.headers(UrlFields.SET_COOKIE);
                String fullCookie = "";
                for (String cookie : cookies) {
                    fullCookie += cookie.split(";")[0] + ";";
                }
                session.setAttribute(SessionFields.VERIFY_CODE_COOKIE, fullCookie);
                try (ServletOutputStream outputStream = resp.getOutputStream()) {
                    ResponseBody body = response.body();
                    BufferedImage read = ImageIO.read(body.byteStream());
                    // 输出到页面
                    ImageIO.write(read, "png", outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
