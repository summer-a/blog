package com.hjb.blog.field;

/**
 * 常用html代码
 *
 * @author 胡江斌
 * @version 1.0
 * @title: HTMLField
 * @projectName blog
 * @description: TODO
 * @date 2019/12/16 18:13
 */
public final class HTMLFields {

    /**
     * 提示添加用户html代码
     */
    public static final String ADD_USER_IF_NOT_EXISTS = "<div style='width:100%;height:50px;line-height:50px;font-size: 36px;text-align: center;'>该用户不存在, 请先添加用户。<a href='https://www.chiyouyun.com/jvtc/page/login'>添加用户</a></div>";

    /**
     * 没找到对应课表的提示信息
     */
    public static final String TABLE_NO_TABLE_MESSAGE = "未找到用户,如未注册请先在https://www.chiyouyun.com/jvtc/page/login网站注册并添加机器人\n询问课表请在机器人设置对应的群或QQ进行课表询问";

    private HTMLFields() {

    }
}
