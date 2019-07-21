package com.hjb.blog.mapper;

import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.normal.JvtcUser;
import tk.mapper.MyMapper;

import java.util.List;

/** jvtc mapper */
public interface JvtcUserMapper extends MyMapper<JvtcUser> {

    /**
     * 获取用户机器人信息
     * @return
     */
    List<UserRobotDTO> selectUserRobotList();
}