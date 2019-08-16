package com.hjb.blog.config;

import com.hjb.blog.interceptor.CommonResourceHandler;
import com.hjb.blog.interceptor.LogInterceptor;
import com.hjb.blog.interceptor.SecurityHandler;
import com.hjb.blog.util.SpringUtils;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 通用web拦截器
 * @author 胡江斌
 * @version 1.0
 * @title: WebAppConfig
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 23:02
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    /** 排除静态资源 */
    private static final String[] EXCLUDE_PATH = new String[]{"/static/**", "/css/**", "/img/**", "/js/**", "/plugin/**"};

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
                .excludePathPatterns(EXCLUDE_PATH);
        // 日志拦截
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH);
        // 管理员页面拦截
        registry.addInterceptor(new SecurityHandler())
                .addPathPatterns("/admin/", "/admin/**")
                .excludePathPatterns(EXCLUDE_PATH);
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
