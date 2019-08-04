package com.hjb.blog.interceptor;

import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.task.TimeTableTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目启动结束处理
 * @author 胡江斌
 * @version 1.0
 * @title: ApplicationRunnerHandler
 * @projectName blog
 * @description: TODO
 * @date 2019/7/20 10:03
 */
@Component
public class ApplicationStartedHandler implements ApplicationRunner {

    @Resource
    private JvtcUserService jvtcUserService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<UserRobotDTO> userRobotList = jvtcUserService.selectUserRobotList();
        TimeTableTask timeTableTask = new TimeTableTask();
        timeTableTask.startByList(userRobotList);
    }
}
