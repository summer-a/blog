package com.hjb.blog.controller.home;

import com.hjb.blog.service.normal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

    @Autowired
    private UserService userService;


}
