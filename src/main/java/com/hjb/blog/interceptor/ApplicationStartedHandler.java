package com.hjb.blog.interceptor;

import com.hjb.blog.service.normal.JvtcUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartedHandler.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("项目初始化");
        /*List<UserRobotDTO> userRobotList = jvtcUserService.selectUserRobotList();
        TimeTableTask timeTableTask = new TimeTableTask();
        timeTableTask.startByList(userRobotList);*/
    }
}
