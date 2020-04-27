package com.hjb.blog.controller.common;

import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.field.CoolqReportFields;
import com.hjb.blog.field.HTMLFields;
import com.hjb.blog.service.normal.RobotService;
import com.hjb.blog.service.normal.UserService;
import com.hjb.blog.util.CoolqUtils;
import com.hjb.blog.util.JvtcLoginUtils;
import com.hjb.blog.util.LoggerUtils;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * QQ消息回报地址
 *
 * @author 胡江斌
 * @version 1.0
 * @title: QQMessageController
 * @projectName blog
 * @description: TODO
 * @date 2019/12/14 10:18
 */
@RestController
@RequestMapping(value = "/qq")
public class QQMessageController {

    @Value("${host.tableUrl}")
    public String tableUrl;

    @Resource
    private RobotService robotService;

    @Resource
    private UserService userService;

    /**
     * 星期匹配
     */
    private Pattern course = Pattern.compile("(本周|这个星期|这星期|该星期)?(课表|课程表)+");
    private Pattern nextCourse = Pattern.compile("(下一周|下周|下个星期|下星期)+");


    /**
     * 消息接收
     *
     * @param data        请求的内容, json, 结构如下
     * <br>message_id   消息id
     * <br>user_id      发送者用户id
     * <br>message     消息主体
     * <br>raw_message  原始消息内容
     * <br>font        字体
     * <br>sender      发送者信息
     * <br>post_type    上报类型
     * <br>message_type 消息类型
     * <br>sub_type     消息子类型，如果是好友则是 friend，如果从群或讨论组来的临时会话则分别是 group、discuss
     * <br>time        发送时间
     * <br>self_id      接收者自己的QQ号
     */
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JSONObject receive(@RequestBody JSONObject data) {
        JSONObject response = new JSONObject();
        // 仅处理自动同意添加好友 和 课表请求
        String postType = data.getStr(CoolqReportFields.POST_TYPE);
        if (Objects.equals(postType, CoolqReportFields.REQUEST)) {
            // 直接同意好友添加请求/邀请入群请求,无备注
            // 群组请求
            if (Objects.equals(data.getStr(CoolqReportFields.REQUEST_TYPE), CoolqReportFields.GROUP)) {
                // 忽略加群请求
                if (Objects.equals(data.getStr(CoolqReportFields.SUB_TYPE), CoolqReportFields.ADD)) {
                    return null;
                }
            }
            LoggerUtils.getLogger().info("{} 邀请'我'加入: {}", data.getStr(CoolqReportFields.USER_ID), data.getStr(CoolqReportFields.GROUP_ID));
            response.put(CoolqReportFields.APPROVE, true);
            return response;
        } else if (Objects.equals(postType, CoolqReportFields.MESSAGE)) {
            LoggerUtils.getLogger().info("{} 发来了一个消息: {}", data.getStr(CoolqReportFields.USER_ID), data.getStr(CoolqReportFields.RAW_MESSAGE));
            // 匹配发送过来的消息中的关键词
            String rawMessage = data.getStr(CoolqReportFields.RAW_MESSAGE);

            // 发送全体消息
            String userId = data.getStr(CoolqReportFields.USER_ID);
            if (Objects.equals(userId, CoolqReportFields.MASTER) && rawMessage.startsWith(CoolqReportFields.PREFIX)) {
                String message = rawMessage.replace(CoolqReportFields.PREFIX, StringUtils.EMPTY);
                try {
                    List<Robot> robots = robotService.selectAll();
                    if (CollectionUtil.isNotEmpty(robots)) {
                        CoolqUtils coolqUtils = CoolqUtils.getInstance();
                        for (Robot robot : robots) {
                            if (Objects.equals(robot.getType(), QQType.QQ.name())) {
                                coolqUtils.sendPrivateMsg(robot.getTarget(), message);
                            } else if (Objects.equals(robot.getType(), QQType.GROUP.name())) {
                                coolqUtils.sendGroupMsg(robot.getTarget(), message);
                            } else if (Objects.equals(robot.getType(), QQType.DISCUSS.name())) {
                                coolqUtils.sendDisCussMsg(robot.getTarget(), message);
                            }
                        }
                    }
                } catch (Exception e) {
                    LoggerUtils.getLogger().error("消息发送失败, 数据:{}, 原因:{}", data, e);
                }
                // 群体消息不和课表同时发送
                return null;
            }

            Matcher courseMatcher = course.matcher(rawMessage);
            if (courseMatcher.find()) {
                // 判断是否有下一周的关键词
                Matcher nextCourseMatcher = nextCourse.matcher(rawMessage);
                // 下一周
                if (nextCourseMatcher.find()) {
                    int howWeeks = JvtcLoginUtils.nowWeek();
                    return setReplayMessage(data, response, (howWeeks + 1));
                }
                // 本周
                else {
                    return setReplayMessage(data, response);
                }
            }
        }
        return null;
    }

    /**
     * 拼装课表链接
     *
     * @param data     源数据
     * @param response 响应数据
     * @return
     */
    private JSONObject setReplayMessage(JSONObject data, JSONObject response) {
        return this.setReplayMessage(data, response, null);
    }

    /**
     * 拼装课表链接
     *
     * @param data     源数据
     * @param response 响应数据
     * @param howWeek  课程周
     * @return
     */
    private JSONObject setReplayMessage(JSONObject data, JSONObject response, Integer howWeek) {
        String messageType = data.getStr(CoolqReportFields.MESSAGE_TYPE);
        String url = tableUrl + "/";
        // 不同消息类型返回对应 qq/群/讨论组 号
        String userId;
        if (Objects.equals(messageType, CoolqReportFields.GROUP)) {
            userId = data.getStr(CoolqReportFields.GROUP_ID);
        } else if (Objects.equals(messageType, CoolqReportFields.DISCUSS)) {
            userId = data.getStr(CoolqReportFields.DISCUSS_ID);
        } else {
            userId = data.getStr(CoolqReportFields.USER_ID);
        }
        JvtcUser jvtcUser = userService.selectUserByRobotTarget(userId);

        if (jvtcUser != null) {
            url += jvtcUser.getUsername();
            // 课程周期
            if (howWeek != null) {
                url += "/" + howWeek;
            }
        } else {
            url = HTMLFields.TABLE_NO_TABLE_MESSAGE;
        }

        response.put(CoolqReportFields.REPLY, url);
        // 拦截其他插件事件
        response.put(CoolqReportFields.BLOCK, true);
        return response;
    }
}
