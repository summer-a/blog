package com.hjb.blog.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * 通用工具
 * @author 胡江斌
 * @version 1.0
 * @title: CommonUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/6/11 18:04
 */
public class CommonUtils {

    private static final String AVATAR_URL = "http://cn.gravatar.com/avatar/%s?s=128&d=identicon&r=PG";

    /**
     * 字符串转为MD5
     */
    public static String toMD5(String str) {
        String md5Str = null;
        if (str != null && str.length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                byte b[] = md.digest();

                int i;
                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                //32位
                md5Str = buf.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return md5Str;
    }

    /**
     * 获取头像
     * @param email 邮箱地址
     * @return
     */
    public static String getGravatar(String email) {
        return String.format(AVATAR_URL, toMD5(email));
    }

    /**
     * 获取客户端真实地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取配置文件的配置
     * @param path 配置文件路径
     * @return
     */
    public static Properties getProperties(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            return PropertiesLoaderUtils.loadProperties(classPathResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Properties properties = getProperties("jdbc.properties");
        String property = properties.getProperty("jdbc.username");
        System.out.println(property);
    }
}