package com.hjb.blog.service.normal;

import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.User;
import com.hjb.blog.service.base.BaseService;

/**
 * 用户服务
 * @author 胡江斌
 * @version 1.0
 * @title: UserService
 * @projectName blog
 * @description: TODO
 * @date 2019/6/9 10:07
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据机器人目标号码找对应的用户
     *
     * @param target
     * @return
     */
    JvtcUser selectUserByRobotTarget(String target);
}
