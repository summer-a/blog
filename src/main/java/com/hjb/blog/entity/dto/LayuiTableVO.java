package com.hjb.blog.entity.dto;

import java.util.List;

/**
 * layui表格数据库
 * @author 胡江斌
 * @version 1.0
 * @title: LayuiTableVO
 * @projectName blog
 * @description: TODO
 * @date 2019/7/5 23:17
 */
public class LayuiTableVO<T> {

    private Integer code;

    private String msg;

    private Integer count;

    private List<T> data;

    public LayuiTableVO() {

    }

    public LayuiTableVO(Integer code, String msg, Integer count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 未登录状态
     * @return
     */
    public static LayuiTableVO notSignedIn() {
        return new LayuiTableVO<>(401, "user not signed in", 0, null);
    }

    @Override
    public String toString() {
        return "LayuiTableVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
