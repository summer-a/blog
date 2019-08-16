package com.hjb.blog.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 页面提交内容过滤工具
 * @author 胡江斌
 * @version 1.0
 * @title: CodeFilterUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/8/5 8:11
 */
public class CodeFilterUtils {

    private static Pattern pattern = Pattern.compile("<[^><]*script[^><]*>");

    /**
     * 替换左右尖括号
     * @param content 内容
     * @return
     */
    public static String replaceGtAndLt(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        return content.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
