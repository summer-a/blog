package com.hjb.blog.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * 用于非Spring管理的bean获取yml配置文件的工具<br>
 *     spring管理的bean使用@Value获取
 * @author 胡江斌
 * @version 1.0
 * @title: YmlUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/7/22 10:30
 */
public class YmlUtils {

    private static final String YML_PATH = "application.yml";

    /**
     * 获取application.yml配置
     * @param key key
     * @return
     */
    public static String get(String key) {
        return get(key, "");
    }

    /**
     * 获取application.yml配置
     * @param key key
     * @param defaultValue 默认值
     * @return
     */
    public static String get(String key, String defaultValue) {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource(YML_PATH));
        Properties prop = yamlPropertiesFactoryBean.getObject();
        return prop.getProperty(key, defaultValue);
    }

}
