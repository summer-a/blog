package com.hjb.blog.config;

import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.interceptor.CommonResourceHandler;
import com.hjb.blog.interceptor.LogInterceptor;
import com.hjb.blog.util.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用web拦截器
 * @author 胡江斌
 * @version 1.0
 * @title: WebAppConfigurer
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 23:02
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    /** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }
    /**
     * 注入拦截器
     * @return
     */
    @Bean
    CommonResourceHandler getCommonResourceHandler() {
        return new CommonResourceHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 通用资源拦截
        registry.addInterceptor(getCommonResourceHandler())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/css/**", "/img/**", "/js/**", "/plugin/**");
        // 日志拦截
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/css/**", "/img/**", "/js/**", "/plugin/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String s) {
                return StringUtils.isEmpty(s) ? null : LocalTime.parse(s);
            }
        });
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String s) {
                return StringUtils.isEmpty(s) ? null : LocalDate.parse(s);
            }
        });
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String s) {
                return StringUtils.isEmpty(s) ? null : LocalDateTime.parse(s);
            }
        });
    }
}
