package com.hjb.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具
 *
 * @author 胡江斌
 * @version 1.0
 * @title: LoggerUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/12/16 18:36
 */
public class LoggerUtils {

    /**
     * 日志（单例）
     */
    private static Logger logger = LoggerFactory.getLogger(LoggerUtils.class);

    private LoggerUtils() {

    }

    public static Logger getLogger() {
        return logger;
    }
}
