package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalTime;

/**
 * 机器人对应用户表实体类
 * @author 胡江斌
 * @version 1.0
 * @title: Robot
 * @projectName blog
 * @description: TODO
 * @date 2019/7/5 23:20
 */
@Data
@NoArgsConstructor
@Table(name = "robot")
public class Robot extends BaseEntity {

    @Column(name = "robot_name")
    private String robotName;

    @Column(name = "jvtc_user_id")
    private Integer jvtcUserId;

    @Column(name = "type")
    private String type;

    @Column(name = "target")
    private Long target;

    @Column(name = "remind_am")
    private LocalTime remindAm;

    @Column(name = "remind_pm")
    private LocalTime remindPm;

    @Column(name = "remind_eve")
    private LocalTime remindEve;

    @Column(name = "status")
    private Integer status;

    public Robot(Integer id) {
        super(id);
    }
}
