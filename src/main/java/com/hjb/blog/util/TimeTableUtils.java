package com.hjb.blog.util;

import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.timetable.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 日志
     */
    private static Logger log = LoggerFactory.getLogger(TimeTableUtils.class);

    /**
     * 一天的秒数
     */
    public static int oneDaySeconds = 1 * 60 * 60 * 24;

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
                } else if (type.equals(QQType.QUN)) {
                    CoolqUtils.getInstance().sendGroupMsg(num, message);
                } else {
                    // 讨论组消息
                    CoolqUtils.getInstance().sendDisCussMsg(num, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("任务计划创建失败。原因：{}", e.getMessage());
            }
        }, delay, TimeUnit.SECONDS);
        log.info("已创建定时! 该任务将在{}小时后执行！", (delay / 3600));
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
            // 多用户同步发送消息
            ScheduledFuture<?> schedule = task.schedule(() -> {
                try {
                    for (MessageInfo messageInfo : messageInfos) {
                        // 消息间隔
                        Properties properties = CommonUtils.getProperties("config.properties");
                        Thread.sleep(Integer.parseInt(properties.getProperty("message.interval", "10")));
                        // 根据类型发送消息
                        if (messageInfo.getType().equals(QQType.QQ)) {
                            // QQ消息
                            CoolqUtils.getInstance().sendPrivateMsg(messageInfo.getNum(), messageInfo.getMessage());
                        } else if (messageInfo.getType().equals(QQType.QUN)) {
                            // 群聊消息
                            CoolqUtils.getInstance().sendGroupMsg(messageInfo.getNum(), messageInfo.getMessage());
                        } else {
                            // 讨论组消息
                            CoolqUtils.getInstance().sendDisCussMsg(messageInfo.getNum(), messageInfo.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("任务计划创建失败。原因：{}", e.getMessage());
                }
            }, delay, TimeUnit.SECONDS);
            log.info("已创建定时! 该任务将在{}小时后执行！", (delay / 3600));
            return schedule;
        }
        return null;
    }
}