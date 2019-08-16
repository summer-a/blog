package com.hjb.blog.service.normal.impl;

import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.mapper.JvtcUserMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.JvtcUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcUserServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/27 23:28
 */
@Service
public class JvtcUserServiceImpl extends BaseServiceImpl<JvtcUser> implements JvtcUserService {

    @Resource
    private JvtcUserMapper jvtcUserMapper;

    /**
     * 获取所有有效的用户对应机器人信息
     *
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Override
    public List<UserRobotDTO> selectUserRobotList() {
        return jvtcUserMapper.selectUserRobotList();
    }
}
