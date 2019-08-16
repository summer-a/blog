package com.hjb.blog.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * elasticsearch基础实体类
 * @author 胡江斌
 * @version 1.0
 * @title: EsBaseEntity
 * @projectName blog
 * @description: TODO
 * @date 2019/8/13 23:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsBaseEntity implements Serializable {

    /** id */
    private Long id;

    /** 内容 */
    private String content;

}
