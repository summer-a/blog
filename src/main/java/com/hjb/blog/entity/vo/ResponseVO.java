package com.hjb.blog.entity.vo;

import lombok.Data;
import okhttp3.Headers;
import us.codecraft.webmagic.selector.Html;

/**
 * 教务系统返回数据结构
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcResponse
 * @projectName blog
 * @description: TODO
 * @date 2019/6/27 20:29
 */
@Data
public class ResponseVO {

    /** 状态码 */
    private Integer code;
    /** html代码 */
    private Html html;
    /** 头描述 */
    private Headers headers;
    /** 缓存 */
    private String cookie;

    public ResponseVO() {}

    public ResponseVO(Integer code, Html html, Headers headers) {
        this.code = code;
        this.html = html;
        this.headers = headers;
    }

}
