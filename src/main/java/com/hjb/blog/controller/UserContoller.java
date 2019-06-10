package com.hjb.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: UserContoller
 * @projectName blog
 * @description: TODO
 * @date 2019/6/9 10:14
 */
@Controller
public class UserContoller {


    @RequestMapping("/")
    public String hello() {
        System.out.println("okkkk");
        return "index";
    }

}
