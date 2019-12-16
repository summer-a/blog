package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.entity.vo.LayuiTableVO;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.field.HTMLFields;
import com.hjb.blog.field.RedisFields;
import com.hjb.blog.field.SessionFields;
import com.hjb.blog.field.UrlFields;
import com.hjb.blog.service.common.RedisService;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.service.normal.RobotService;
import com.hjb.blog.util.JvtcLoginUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 机器人报课控制器
 * @author 胡江斌
 * @version 1.0
 * @title: TimeTableController
 * @projectName blog
 * @description: TODO
 * @date 2019/6/25 20:26
 */
@Controller
@RequestMapping("/timetable")
public class TimeTableController {

    @Resource
    private RobotService robotService;

    @Resource
    private JvtcUserService jvtcUserService;

    @Resource
    private RedisService redisService;

    /**
     * 管理页主页
     * @return
     */
    @GetMapping(value = {"/index"})
    public String timeTableManager() {
        return "Home/Page/timeTableManager";
    }

    /**
     * 根据id获取机器人报课表
     * @param id id
     * @return
     */
    @GetMapping(value = "/addOrEditPage")
    public String addOrEditPage(@RequestParam(value = "id", required = false) Integer id,
                                Model model) {
        if (id != null) {
            Robot robot = robotService.selectOne(new Robot(id));
            model.addAttribute(SessionFields.JVTC_ROBOT_INFO, robot);
        }

        return "Home/Model/addOrEditRobot";
    }

    /**
     * 获取机器人提醒列表
     * @param pageNum 开始页
     * @param pageSize 每页数量
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getRobotMindList")
    public LayuiTableVO<Robot> getRobotMindList(
            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            HttpServletRequest request) {
        JvtcUser user = JvtcLoginUtils.getJvtcUser(request);
        if (user == null) {
            return LayuiTableVO.notSignedIn();
        }
        Robot robot = new Robot();
        robot.setJvtcUserId(user.getId());
        // 根据id分页排序
        PageInfo<Robot> list = robotService.page(pageNum, pageSize, robot, OrderField.orderByAsc("id"));

        LayuiTableVO<Robot> layuiTableVO = new LayuiTableVO<>();
        layuiTableVO.setCode(0);
        layuiTableVO.setCount((int) list.getTotal());
        layuiTableVO.setMsg("ok");
        layuiTableVO.setData(list.getList());

        return layuiTableVO;
    }

    /**
     * 添加或更新报课，根据是否有id进行判断
     * @param robot 报课机器人信息
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/addOrUpdateRobot")
    public ResultVO addOrUpdateRobot(Robot robot, HttpServletRequest request) {

        JvtcUser jvtcUser = JvtcLoginUtils.getJvtcUser(request);
        if (jvtcUser == null) {
            return ResultVO.build(401, "账号信息已过期，请重新登录！", null);
        }

        robot.setJvtcUserId(jvtcUser.getId());

        if (robot != null && robot.getId() != null) {
            // 更新状态？
            robotService.update(robot);
        } else {
            if (!jvtcUser.getVip()) {
                Robot t = new Robot();
                t.setJvtcUserId(jvtcUser.getId());
                List<Robot> list = robotService.select(t);
                if (!jvtcUser.getVip() && !CollectionUtils.isEmpty(list) && list.size() >= 1) {
                    return ResultVO.fail("普通用户只能添加一个机器人");
                }
            }
            // 添加任务计划
            robotService.insertSelective(robot);

        }
        return ResultVO.ok();
    }

    /**
     * 根据id删除
     * @param id id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/deleteRobot")
    public ResultVO deleteRobot(@RequestParam(value = "id", required = true) Integer id) {
        // 删除操作
        robotService.deleteByPrimaryKey(id);
        // 移除消息队列(更改了实现方式)
        //...
        return ResultVO.ok();
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SessionFields.JVTC_USER);
        session.invalidate();
        return "redirect:/jvtc/page/login";
    }

    /**
     * 课表页面
     * @param id id
     * @param week 周次
     * @param refresh 是否强制刷新
     * @throws IOException
     */
    @GetMapping(value = {"/{id}", "/{id}/{week}", "/{id}/{week}/{refresh}"})
    public String page(Model model,
                       @PathVariable String id,
                       @PathVariable(required = false) Integer week,
                       @PathVariable(required = false) Boolean refresh) {

        // 默认本周
        week = week == null ? JvtcLoginUtils.howWeeks(LocalDate.now()) : week;

        JvtcUser userParam = new JvtcUser();
        userParam.setUsername(id);
        JvtcUser jvtcUser = jvtcUserService.selectOne(userParam);
        if (jvtcUser == null) {
            model.addAttribute(SessionFields.TABLE_HTML, HTMLFields.ADD_USER_IF_NOT_EXISTS);
        } else {
            // 删除缓存
            if (refresh != null && refresh) {
                redisService.delete(String.format(RedisFields.TABLE, id, StringUtils.isEmpty(week) ? JvtcLoginUtils.howWeeks(LocalDate.now()) : week));
            }
            Html timeTable = JvtcLoginUtils.getTimeTable(week, jvtcUser, 3);
            // 强制刷新链接
            model.addAttribute(SessionFields.TABLE_REFRESH_URL, String.format(UrlFields.TABLE_REFRESH, id, week));
            model.addAttribute(SessionFields.TABLE_HTML, timeTable.get());
        }
        return "Home/Other/timeTable";
    }
}
