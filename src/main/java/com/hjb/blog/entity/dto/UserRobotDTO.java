package com.hjb.blog.entity.dto;

import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import lombok.Data;

import java.util.List;

/**
 * 用户机器人对应实体类
 * @author 胡江斌
 * @version 1.0
 * @title: UserRobotDTO
 * @projectName blog
 * @description: TODO
 * @date 2019/7/19 16:46
 */
@Data
public class UserRobotDTO {

    private JvtcUser jvtcUser;

    private List<Robot> robotList;

}
