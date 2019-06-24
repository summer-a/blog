package com.hjb.blog.entity.dto;

import java.io.Serializable;

/**
 * 通用结果集
 * @author 胡江斌
 * @version 1.0
 * @title: ResultVO
 * @projectName blog
 * @description: TODO
 * @date 2019/6/18 15:22
 */
public class ResultVO<T> implements Serializable{

    private Integer code;

    private String msg;

    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> ResultVO<T> ok() {
        return ok("ok");
    }

    public static <T> ResultVO<T> ok(String msg) {
        return ok(msg, null);
    }

    public static <T> ResultVO<T> ok(String msg, T data) {
        return build(200, msg, data);
    }

    public static <T> ResultVO<T> fail() {
        return new ResultVO<>(500, "fail", null);
    }

    public static <T> ResultVO<T> fail(String msg) {
        return new ResultVO<>(500, msg, null);
    }

    public static <T> ResultVO<T> fail(String msg, T data) {
        return new ResultVO<>(500, msg, data);
    }

    public static <T> ResultVO<T> build(Integer code, String msg, T data) {
        return new ResultVO<>(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
