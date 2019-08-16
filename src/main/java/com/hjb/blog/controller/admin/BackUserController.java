package com.hjb.blog.controller.admin;


import com.hjb.blog.entity.normal.User;
import com.hjb.blog.service.normal.UserService;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/user")
public class BackUserController {

    @Autowired
    private UserService userService;

    /**
     * 后台用户列表显示
     *
     * @return
     */
    @GetMapping(value = "")
    public ModelAndView userList() {
        ModelAndView modelandview = new ModelAndView();

        List<User> userList = userService.selectAll();
        modelandview.addObject("userList", userList);

        modelandview.setViewName("Admin/User/index");
        return modelandview;

    }

    /**
     * 后台添加用户页面显示
     *
     * @return
     */
    @GetMapping(value = "/insert")
    public ModelAndView insertUserView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Admin/User/insert");
        return modelAndView;
    }

    /**
     * 检查用户名是否存在
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/checkUserName")
    @ResponseBody
    public String checkUserName(HttpServletRequest request, Integer id, String username) {
        Map<String, Object> map = new HashMap<>(2);
        User param = new User();
        param.setUserName(username);
        User user = userService.selectOne(param);
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if (user != null) {
            if (user.getId() != id) {
                map.put("code", 1);
                map.put("msg", "用户名已存在！");
            }
        } else {
            map.put("code", 0);
            map.put("msg", "");
        }
        String result = new JSONObject(map).toString();
        return result;
    }

    /**
     * 检查Email是否存在
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/checkUserEmail")
    @ResponseBody
    public String checkUserEmail(HttpServletRequest request, Integer id, String email) {
        Map<String, Object> map = new HashMap<>(2);
        User param = new User();
        param.setUserEmail(email);
        User user = userService.selectOne(param);
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if (user != null) {
            if (user.getId() != id) {
                map.put("code", 1);
                map.put("msg", "电子邮箱已存在！");
            }
        } else {
            map.put("code", 0);
            map.put("msg", "");
        }
        String result = new JSONObject(map).toString();
        return result;
    }


    /**
     * 后台添加用户页面提交
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/insertSubmit")
    public String insertUserSubmit(User user) {
        User paramByName = new User();
        paramByName.setUserName(user.getUserName());
        User userByName = userService.selectOne(paramByName);

        if (userByName == null) {
            user.setCreateTime(LocalDateTime.now());
            user.setUserStatus(1);
            userService.insertSelective(user);
        }
        return "redirect:/admin/user";
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteByPrimaryKey(id);
        return "redirect:/admin/user";
    }

    /**
     * 编辑用户页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public ModelAndView editUserView(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.selectById(id);
        modelAndView.addObject("user", user);

        modelAndView.setViewName("Admin/User/edit");
        return modelAndView;
    }


    /**
     * 编辑用户提交
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/editSubmit")
    public String editUserSubmit(User user) {
        userService.update(user);
        return "redirect:/admin/user";
    }

    /**
     * 基本信息页面显示
     *
     * @return
     */
    @GetMapping(value = "/profile")
    public ModelAndView userProfileView(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.selectById(sessionUser.getId());
        modelAndView.addObject("user", user);

        modelAndView.setViewName("Admin/User/profile");
        return modelAndView;
    }
}
