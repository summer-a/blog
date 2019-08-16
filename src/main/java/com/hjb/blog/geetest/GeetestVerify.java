package com.hjb.blog.geetest;

import com.hjb.blog.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 极验验证类
 * @author 胡江斌
 * @version 1.0
 * @title: GeetestVerify
 * @projectName blog
 * @description: TODO
 * @date 2019/8/5 15:58
 */
public class GeetestVerify {

    private static Properties config = CommonUtils.getProperties("config.properties");

    private static GeetestLib gtSdk = new GeetestLib(config.getProperty("geetest.id"), config.getProperty("geetest.key"),
            Boolean.parseBoolean(config.getProperty("geetest.newfailback")));

    /**
     * 二次验证
     * @param request
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(HttpServletRequest request, String user) throws UnsupportedEncodingException {

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        HttpSession session = request.getSession();

        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) session.getAttribute(gtSdk.gtServerStatusSessionKey);

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>(2);
        //传输用户请求验证时所携带的IP
        // 网站用户id
        param.put("user_id", user == null ? "user" : user);
        // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        String userAgent = request.getHeader("User-Agent");
        param.put("client_type", userAgent.contains("Windows") ? "web" : "h5");
        // 请求的用户的ip
        param.put("ip_address", CommonUtils.getIpAddr(request));

        int gtResult = 0;
        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }

        return gtResult == 1 ? true : false;
    }
}
