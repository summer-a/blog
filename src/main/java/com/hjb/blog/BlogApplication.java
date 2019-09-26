package com.hjb.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(basePackages = "com.hjb.blog.mapper", properties = "mappers=tk.mybatis.mapper.common.Mapper")
public class BlogApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BlogApplication.class);
    }

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(BlogApplication.class, args);
    }

}
