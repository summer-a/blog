package com.hjb.blog.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBean工具
 * @author 胡江斌
 * @version 1.0
 * @title: SpringUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/7/21 10:22
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String beanId) {
        return applicationContext.getBean(clazz, beanId);
    }
}
