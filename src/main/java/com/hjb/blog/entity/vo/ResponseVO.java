package com.hjb.blog.entity.vo;

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
public class ResponseVO {

    /** 状态码 */
    private Integer code;
    /** html代码 */
    private Html html;
    /** 头描述 */
    private Headers headers;

    public ResponseVO() {}

    public ResponseVO(Integer code, Html html, Headers headers) {
        this.code = code;
        this.html = html;
        this.headers = headers;
    }

    public Html getHtml() {
        return html;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "code=" + code +
                ", html=" + html +
                ", headers=" + headers +
                '}';
    }
}
