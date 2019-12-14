package com.hjb.blog.controller.common;

import com.hjb.blog.util.JvtcLoginUtils;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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

    /**
     * 星期匹配
     */
    private Pattern course = Pattern.compile("(本周|这个星期|这星期|该星期)?(课表|课程表)+");
    private Pattern nextCourse = Pattern.compile("(下一周|下周|下个星期|下星期)+");


    /**
     * 消息接收
     *
     * @param data        请求的内容, json, 结构如下
     * @param messageId   消息id
     * @param userId      发送者用户id
     * @param message     消息主体
     * @param rawMessage  原始消息内容
     * @param font        字体
     * @param sender      发送者信息
     * @param postType    上报类型
     * @param messageType 消息类型
     * @param subType     消息子类型，如果是好友则是 friend，如果从群或讨论组来的临时会话则分别是 group、discuss
     * @param time        发送时间
     * @param selfId      接收者自己的QQ号
     */
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JSONObject receive(@RequestBody JSONObject data) {
        JSONObject response = new JSONObject();
        // 仅处理自动同意添加好友 和 课表请求
        String postType = data.getStr("post_type");
        if (Objects.equals(postType, "request")) {
            // 直接同意好友添加请求/邀请入群请求,无备注
            // 群组请求
            if (Objects.equals(data.getStr("request_type"), "group")) {
                // 忽略加群请求
                if (Objects.equals(data.getStr("sub_type"), "add")) {
                    return null;
                }
            }
            response.put("approve", true);
            return response;
        } else if (Objects.equals(postType, "message")) {
            // 匹配发送过来的消息中的关键词
            String rawMessage = data.getStr("raw_message");
            Matcher courseMatcher = course.matcher(rawMessage);
            if (courseMatcher.find()) {
                // 判断是否有下一周的关键词
                Matcher nextCourseMatcher = nextCourse.matcher(rawMessage);
                // 下一周
                if (nextCourseMatcher.find()) {
                    int howWeeks = JvtcLoginUtils.howWeeks(LocalDate.now());
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
        String messageType = data.getStr("message_type");
        String url = tableUrl + "/";
        // 不同消息类型返回对应 qq/群/讨论组 号
        if (Objects.equals(messageType, "group")) {
            url += data.getStr("group_id");
        } else if (Objects.equals(messageType, "discuss")) {
            url += data.getStr("discuss_id");
        } else {
            url += data.getStr("user_id");
        }
        // 课程周期
        if (howWeek != null) {
            url += "/" + howWeek;
        }
        response.put("reply", url);
        return response;
    }
}
