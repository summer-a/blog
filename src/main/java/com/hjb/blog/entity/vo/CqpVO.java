package com.hjb.blog.entity.vo;

import java.util.Map;

/**
 * 酷Q机器人响应结构
 *
 * @author 胡江斌
 * @version 1.0
 * @title: CqpVO
 * @projectName blog
 * @description: TODO
 * @date 2019/6/25 0:13
 */
public class CqpVO {

    private Integer retcode;

    private String status;

    private Map<String, Object> data;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CqpVO{" +
                "retcode=" + retcode +
                ", status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
