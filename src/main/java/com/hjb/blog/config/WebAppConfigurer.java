package com.hjb.blog.config;

import com.hjb.blog.interceptor.CommonResourceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

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
        registry.addInterceptor(getCommonResourceHandler()).
                addPathPatterns("/**")
                .excludePathPatterns("/static/**");

    }

}
