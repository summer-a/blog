package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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
public class Robot extends BaseEntity {

    @NotNull(message = "机器人名不能为空")
    @Column(name = "robot_name")
    private String robotName;

    @NotNull(message = "用户id不能为空")
    @Column(name = "jvtc_user_id")
    private Integer jvtcUserId;

    @Column(name = "type")
    private String type;

    @NotNull(message = "目标号码不能为空")
    @Column(name = "target")
    private Integer target;

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
