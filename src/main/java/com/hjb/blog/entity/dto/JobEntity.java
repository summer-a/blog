package com.hjb.blog.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: JobEntity
 * @projectName blog
 * @description: TODO
 * @date 2019/9/18 21:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JobEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String jobGroup;

    private String cron;

    private String parameter;

    /**
     * 延时时间
     */
    private Integer delaySecond;

    /**
     * job的执行状态,这里我设置为1/0且只有该值为1才会执行该Job
     */
    private Integer status = 1;

    public JobEntity(String name, String jobGroup, Integer delaySecond, Integer status) {
        this.name = name;
        this.jobGroup = jobGroup;
        this.delaySecond = delaySecond;
        this.status = status;
    }

    public JobEntity() {
    }
}