package com.hjb.blog.util;

import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.timetable.MessageInfo;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 课表工具
 *
 * @author 胡江斌
 * @version 1.0
 * @title: TimeTableUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/7/9 17:13
 */
public class TimeTableUtils {

    /**
     * 每天执行时间 (6:00)
     */
    public static int firstTime = LocalTime.of(6, 0).toSecondOfDay();

    /**
     * 任务线程池
     */
    public static ScheduledExecutorService task = new ScheduledThreadPoolExecutor(10);

    /**
     * 创建任务
     *
     * @param num     号码
     * @param type    目标类型
     * @param message 消息
     * @param delay   延时时间
     */
    public static ScheduledFuture<?> createSchedule(int num, QQType type, String message, int delay) {
        // 发送消息
        ScheduledFuture<?> schedule = task.schedule(() -> {
            try {
                // 根据类型发送消息
                if (type.equals(QQType.QQ)) {
                    CoolqUtils.getInstance().sendPrivateMsg(num, message);
                } else if (type.equals(QQType.GROUP)) {
                    CoolqUtils.getInstance().sendGroupMsg(num, message);
                } else {
                    // 讨论组消息
                    CoolqUtils.getInstance().sendDisCussMsg(num, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LoggerUtils.getLogger().error("任务计划创建失败。原因：{}", e.getMessage());
            }
        }, delay, TimeUnit.SECONDS);
        LoggerUtils.getLogger().info("已创建定时! 该任务将在{}小时后执行！", (delay / 3600));
        return schedule;
    }

    /**
     * 多用户消息发送
     * @param messageInfos
     * @param delay
     * @return
     */
    public static ScheduledFuture<?> createScheduleList(List<MessageInfo> messageInfos, int delay) {
        if (!CollectionUtils.isEmpty(messageInfos)) {
            // 超时任务不开启
            if (delay >= 0) {
                // 多用户同步发送消息
                ScheduledFuture<?> schedule = task.schedule(() -> {
                    // 发送
                    send(messageInfos);
                }, delay, TimeUnit.SECONDS);
                LoggerUtils.getLogger().info("已创建定时! 该任务将在{}执行！", LocalTime.now().plusSeconds(delay).format(DateTimeFormatter.ISO_LOCAL_TIME));
                return schedule;
            }
        }
        return null;
    }

    /**
     * 发送
     * @param messageInfos
     */
    public static void send(List<MessageInfo> messageInfos) {
        try {
            for (MessageInfo messageInfo : messageInfos) {
                // 消息间隔
                Properties properties = CommonUtils.getProperties();
                Thread.sleep(Integer.parseInt(properties.getProperty("message.interval", "10")));
                // 根据类型发送消息
                if (messageInfo.getType().equals(QQType.QQ)) {
                    // QQ消息
                    CoolqUtils.getInstance().sendPrivateMsg(messageInfo.getNum(), messageInfo.getMessage());
                } else if (messageInfo.getType().equals(QQType.GROUP)) {
                    // 群聊消息
                    CoolqUtils.getInstance().sendGroupMsg(messageInfo.getNum(), messageInfo.getMessage());
                } else {
                    // 讨论组消息
                    CoolqUtils.getInstance().sendDisCussMsg(messageInfo.getNum(), messageInfo.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.getLogger().error("任务计划创建失败。原因：{}", e.getMessage());
        }
    }
}
