package com.hjb.blog.service.common.impl;

import com.hjb.blog.entity.jvtc.JvtcCourse;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.service.common.CourseService;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.JvtcAppApiUtils;
import com.hjb.blog.util.LoggerUtils;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: CourseServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/11/23 15:53
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private JvtcUserService jvtcUserService;

    /**
     * 获取课程表
     *
     * @param user        用户
     * @param date        日期
     * @param retryCounts 重试次数
     * @return
     */
    @Override
    @Cacheable(
            value = "table",
            key = "#user.username + ':::' + #date",
            condition = "#user != null and #date != null",
            unless = "#result == null or #result.size() <= 1"
    )
    public List<JvtcCourse> selectCourseIfSignedOrNot(JvtcUser user, String date, int retryCounts) {
        LoggerUtils.getLogger().info(user.getUsername() + "->获取课程表");
        String success = "success";
        String token = "token";
        String xnxqh = "xnxqh";
        String zc = "zc";
        String fail = "-1";
        /* 直接获取课程 */
        // 获取服务器时间
        if (!StringUtils.isEmpty(user.getCookie())) {
            JSONObject currentTime = JvtcAppApiUtils.getCurrentTime(user.getCookie(), date);
            if (!currentTime.isEmpty() && !Objects.equals(currentTime.getStr(token), fail)) {

                JSONArray kcb = JvtcAppApiUtils.getKcb(user.getCookie(), user.getUsername(), currentTime.getStr(xnxqh), currentTime.getStr(zc));

                if (!kcb.isEmpty() && !Objects.equals(currentTime.getStr(token), fail) && kcb.size() > 1) {
                    return kcb.toList(JvtcCourse.class);
                }
            }
        }

        // 获取时间失败则重新认证用户
        JSONObject response = JvtcAppApiUtils.authUser(user.getUsername(), user.getPassword());
        if (!response.isEmpty() && response.getBool(success, false)) {

            String userToken = response.getStr(token);

            // 存入数据库
            JvtcUser jvtcUser = new JvtcUser();
            jvtcUser.setCookie(userToken);
            Example example = new Example(JvtcUser.class);
            example.createCriteria().andEqualTo("id", user.getId());
            jvtcUserService.updateByExampleSelective(jvtcUser, example);

            user.setCookie(userToken);

            if (retryCounts >= 1) {
                LoggerUtils.getLogger().info(user.getUsername() + "->获取课表失败, 进行重试");
                return selectCourseIfSignedOrNot(user, date, --retryCounts);
            }
        }

        return new ArrayList<>();
    }

}
