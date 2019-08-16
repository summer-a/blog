package com.hjb.blog.controller.common;

import com.hjb.blog.geetest.GeetestLib;
import com.hjb.blog.util.CommonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Properties;

/**
 * 极验验证控制器
 *
 * @author 胡江斌
 * @version 1.0
 * @title: GeetestController
 * @projectName blog
 * @description: TODO
 * @date 2019/8/5 11:45
 */
@RestController
@RequestMapping("/geetest")
public class GeetestController {

    private Properties config = CommonUtils.getProperties("config.properties");

    private GeetestLib gtSdk = new GeetestLib(config.getProperty("geetest.id"), config.getProperty("geetest.key"),
            Boolean.parseBoolean(config.getProperty("geetest.newfailback")));

    /**
     * 验证
     *
     * @param request
     */
    @GetMapping("/register")
    public String startCaptcha(HttpServletRequest request,
                               @RequestParam(value = "userEmail", required = false, defaultValue = "user") String userEmail) {

        // 结果
        String resStr = "{}";

        // 自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>(2);
        // 传输用户请求验证时所携带的IP
        // 网站用户id
        param.put("user_id", userEmail);
        // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        String userAgent = request.getHeader("User-Agent");
        param.put("client_type", userAgent.contains("Windows") ? "web" : "h5");
        // 请求的用户的ip
        param.put("ip_address", CommonUtils.getIpAddr(request));

        // 进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        // 将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);

        resStr = gtSdk.getResponseStr();

        return resStr;
    }

}
