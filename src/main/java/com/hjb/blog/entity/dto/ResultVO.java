package com.hjb.blog.entity.dto;

/**
 * @author h1525
 * @version 1.0
 * @title: ResultVO
 * @projectName blog
 * @description: TODO 结果结构体
 * @date 2019/6/8 17:23
 */
public class ResultVO<T> {

    /** 错误码 */
    private Integer code;
    /** 错误信息 */
    private String msg;
    /** 返回对象 */
    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVO ok() {
        return new ResultVO(200, "ok", null);
    }

    public static ResultVO ok(String msg) {
        return new ResultVO(200, msg, null);
    }

    public static <T> ResultVO ok(String msg, T data) {
        return new ResultVO(200, msg, data);
    }

    public static ResultVO fail() {
        return  new ResultVO(500, "fail", null);
    }
    public static ResultVO fail(String msg) {
        return new ResultVO(500, msg, null);
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
        return "ResultVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
