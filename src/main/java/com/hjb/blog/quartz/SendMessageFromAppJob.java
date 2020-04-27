package com.hjb.blog.quartz;

import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.jvtc.JvtcCourse;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.entity.timetable.MessageInfo;
import com.hjb.blog.service.common.SyllabusService;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.TimeTableUtils;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 从app获取用户和课表信息
 *
 * @author 胡江斌
 * @version 1.0
 * @title: SendMessageFromApp
 * @projectName blog
 * @description: TODO
 * @date 2019/11/21 0:20
 */
@Component
//@EnableScheduling
// 除了登录接口,其他接口被废弃
public class SendMessageFromAppJob {

    private Logger logger = Logger.getLogger(SendMessageFromAppJob.class);

    @Resource
    private JvtcUserService jvtcUserService;

    @Resource
    private SyllabusService syllabusService;

    @Value("${host.tableUrl}")
    private String TABLE_URL;

    /**
     * 是否推送课表
     */
    @Value("${course.push}")
    private Boolean COURSE_PUSH;


    @Scheduled(cron = "0 0 0 ? * MON-FRI")
    public void initCourse() {
        logger.info("初始化课表");

        List<UserRobotDTO> userRobots = jvtcUserService.selectUserRobotList();
        // 更新没存入redis的课表
        for (UserRobotDTO userRobot : userRobots) {
            syllabusService.selectCourseIfSignedOrNot(userRobot.getJvtcUser(), LocalDate.now().format(DateTimeFormatter.ISO_DATE), 3);
        }
    }

    @Scheduled(cron = "0 10 7 ? * MON-FRI")
    public void sendInTheMorning() {
        if (COURSE_PUSH != null && COURSE_PUSH) {
            logger.info("发送课表(上午)");
            send(1, false);
        }
    }

    @Scheduled(cron = "0 0 13 ? * MON-FRI")
    public void sendInTheNoon() {
        if (COURSE_PUSH != null && COURSE_PUSH) {
            logger.info("发送课表(中午)");
            send(2, false);
        }
    }

    @Scheduled(cron = "0 0 17 ? * MON-FRI")
    public void sendInTheAfterNoon() {
        if (COURSE_PUSH != null && COURSE_PUSH) {
            logger.info("发送课表(下午)");
            send(3, true);
        }
    }

    @Scheduled(cron = "0 20 18 ? * MON-FRI")
    public void sendInTheEvening() {
        if (COURSE_PUSH != null && COURSE_PUSH) {
            logger.info("发送课表(晚上)");
            send(3, false);
        }
    }

    private void send(int interval, boolean isFifthLesson) {
        List<UserRobotDTO> userRobots = jvtcUserService.selectUserRobotList();
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (UserRobotDTO userRobot : userRobots) {
            JvtcUser jvtcUser = userRobot.getJvtcUser();
            List<JvtcCourse> courses = syllabusService.selectCourseIfSignedOrNot(jvtcUser, LocalDate.now().format(DateTimeFormatter.ISO_DATE), 3);

            if (!CollectionUtil.isEmpty(courses)) {
                // 过滤当天星期的课
                courses = courses.stream().filter(
                        course -> course != null && course.getKcsj()[0] != null && course.getKcsj()[0] == LocalDate.now().getDayOfWeek().getValue()).collect(Collectors.toList());

                if (!CollectionUtil.isEmpty(courses)) {

                    // 判断是否是本周的课?
                    // 01(代表中/下/晚的第一节课)
                    Optional<JvtcCourse> t1 = courses.stream().filter(r -> r.getKcsj()[1] == interval * 4 - 3).findFirst();
                    // 02(代表中/下/晚的第二节课)
                    Optional<JvtcCourse> t2 = courses.stream().filter(r -> r.getKcsj()[1] == interval * 4 - 1).findFirst();

                    if (interval == 3) {
                        if (isFifthLesson && !t1.isPresent()) {
                            continue;
                        } else if (!isFifthLesson && t1.isPresent()) {
                            continue;
                        }
                    }
                    // 如果是第五节大课,提前报课
                    //
                    if (t1.isPresent() || t2.isPresent()) {
                        String tableText = createTableText(t1) + createTableText(t2);
                        this.appendInfo(messageInfos, tableText, userRobot);
                    }
                }
            }
        }
        //
        TimeTableUtils.send(messageInfos);
    }

    private String createTableText(Optional<JvtcCourse> course) {
        if (course.isPresent()) {
            try {
                StringBuilder sb = new StringBuilder();
                JvtcCourse jvtcCourse = course.orElseThrow(NullPointerException::new);
                sb.append("----------------------------\n");
                sb.append(String.format("第%d节课\n", jvtcCourse.getKcsj()[1] / 2 + 1));
                sb.append(String.format("课程名称：%s\n", jvtcCourse.getKcmc()));
                sb.append(String.format("上课地点：%s\n", jvtcCourse.getJsmc()));
                sb.append(String.format("上课时间：%s\n", jvtcCourse.getKssj()));
                sb.append(String.format("上课老师：%s\n", jvtcCourse.getJsxm()));
                sb.append("----------------------------\n");
                return sb.toString();
            } catch (Exception e) {

            }
        }
        return "";
    }

    /**
     * 追加信息
     *
     * @param course
     * @param tableText
     * @param userRobot
     */
    private void appendInfo(List<MessageInfo> course, String tableText, UserRobotDTO userRobot) {
        List<Robot> robotList = userRobot.getRobotList();
        if (!CollectionUtils.isEmpty(robotList)) {
            for (Robot robot : robotList) {
                MessageInfo mi = new MessageInfo();
                mi.setNum(robot.getTarget());
                mi.setType(QQType.valueOf(robot.getType()));
                mi.setMessage(tableText);
                course.add(mi);
            }
        }
    }
}
