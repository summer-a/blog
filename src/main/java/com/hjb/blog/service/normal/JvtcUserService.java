package com.hjb.blog.service.normal;

import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.service.base.BaseService;

import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcUserService
 * @projectName blog
 * @description: TODO
 * @date 2019/6/27 23:27
 */
public interface JvtcUserService extends BaseService<JvtcUser> {

    /**
     * 获取所有有效的用户对应机器人信息
     * @return
     */
    List<UserRobotDTO> selectUserRobotList();
}
