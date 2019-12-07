package com.hjb.blog.service.common;

import com.hjb.blog.entity.jvtc.JvtcCourse;
import com.hjb.blog.entity.normal.JvtcUser;

import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: CourseService
 * @projectName blog
 * @description: TODO
 * @date 2019/11/23 15:50
 */
public interface CourseService {

    /**
     * 获取课程表
     *
     * @param user        用户
     * @param date        日期
     * @param retryCounts 重试次数
     * @return
     */
    List<JvtcCourse> selectCourseIfSignedOrNot(JvtcUser user, String date, int retryCounts);
}
