package com.hjb.blog.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Headers;

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
@NoArgsConstructor
public class ResponseVO {

    /** 状态码 */
    private Integer code;
    /** html代码 */
    private String html;
    /** 头描述 */
    private Headers headers;
    /** 缓存 */
    private String cookie;

    public ResponseVO(Integer code, String html, Headers headers) {
        this.code = code;
        this.html = html;
        this.headers = headers;
    }

}
