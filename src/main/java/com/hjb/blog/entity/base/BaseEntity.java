package com.hjb.blog.entity.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * @author 胡江斌
 * @version 1.0
 * @title: BaseEntity
 * @projectName blog
 * @description: TODO
 * @date 2019/6/8 14:14
 */
@Data
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public BaseEntity(Integer id) {
        this.id = id;
    }

}
