package com.hjb.blog.interceptor;

import com.hjb.blog.util.LoggerUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LoggerUtils.getLogger().info("项目初始化");
    }
}
