package com.hjb.blog.task;

import com.hjb.blog.config.WebAppConfigurer;
import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.entity.timetable.MessageInfo;
import com.hjb.blog.entity.timetable.TimeTablePerTime;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.JvtcLoginUtils;
import com.hjb.blog.util.SpringUtils;
import com.hjb.blog.util.TimeTableUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.selector.Html;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 消息任务
 *
 * @author h1525
 */
public class TimeTableTask {

    /**
     * 日志
     */
    private Logger log = LoggerFactory.getLogger(TimeTableTask.class);


    /**
     * 任务计划列表
     */
    public static Map<Integer, List<ScheduledFuture>> scheduleds = new HashMap<>(10);

    /**
     * 每节课上课时间，记录的是每天经过的时间（单位秒,0为第一大节课）
     * 记得更新作息表
     */
    private static LocalTime[] time = {
            LocalTime.of(8, 5),
            LocalTime.of(10, 20),
            LocalTime.of(14, 35),
            LocalTime.of(16, 25),
            LocalTime.of(18, 5),
            LocalTime.of(19, 45)
    };


    private static DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HH:mm");

    private static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String line = "-------------------------------------------\n";

    /**
     * 上午提醒时间(默认)
     */
    private LocalTime am = LocalTime.of(7, 10);
    /**
     * 下午提醒时间(默认)
     */
    private LocalTime pm = LocalTime.of(13, 0);
    /**
     * 晚上提醒时间(默认)
     */
    private LocalTime ni = LocalTime.of(18, 20);

    /**
     * 使用默认的提醒时间
     */
    public TimeTableTask() {
    }

    /**
     * 初始化提醒事件
     *
     * @param am 早上提醒时间
     * @param pm 中午提醒时间
     * @param ni 晚上提醒时间
     */
    public TimeTableTask(LocalTime am, LocalTime pm, LocalTime ni) {
        this.am = am == null ? this.am : am;
        this.pm = pm == null ? this.pm : pm;
        this.ni = ni == null ? this.ni : ni;
    }

    /**
     * 组团发送
     *
     * @param userRobots
     * @param timeTableUrl
     */
    public void startByList(List<UserRobotDTO> userRobots, String timeTableUrl) {

        // 每天执行
        int now = LocalTime.now().toSecondOfDay();
        int exetime = TimeTableUtils.firstTime - now;
        // 如果超时则第二天执行
        exetime = exetime < 0 ? exetime + TimeTableUtils.oneDaySeconds : exetime;

        log.info("课程提醒已开启!,首次任务将在 {} 后执行", LocalTime.ofSecondOfDay(exetime).format(DateTimeFormatter.ofPattern("HH小时mm分")));

        // 每天执行一次
        TimeTableUtils.task.scheduleAtFixedRate(() -> {

            List<MessageInfo> amCourse = new ArrayList<>();
            List<MessageInfo> pmCourse = new ArrayList<>();
            List<MessageInfo> eveCourse = new ArrayList<>();

            // 今天
            String today = LocalDate.now().format(yyyyMMdd);
            // 获取当天课程表字符串(一个星期)
            // 拿到所有有效用户的课表, 根据班级从缓存获取
            for (UserRobotDTO userRobot : userRobots) {
                JvtcUser juser = userRobot.getJvtcUser();
                Html timeTable = JvtcLoginUtils.getTimeTable(today, juser.getUsername(), juser.getPassword(), juser.getCookie());
                // 转成对象(一天)
                List<TimeTablePerTime> courseOfDay = htmlToBeen(timeTable, LocalDate.now().getDayOfWeek().getValue());

                // 获取课程表
                if (courseOfDay.size() > 0) {

                    Optional<TimeTablePerTime> t1 = courseOfDay.stream().filter(f -> f.getNo() == 1).findFirst();
                    Optional<TimeTablePerTime> t2 = courseOfDay.stream().filter(f -> f.getNo() == 2).findFirst();
                    Optional<TimeTablePerTime> t3 = courseOfDay.stream().filter(f -> f.getNo() == 3).findFirst();
                    Optional<TimeTablePerTime> t4 = courseOfDay.stream().filter(f -> f.getNo() == 4).findFirst();
                    Optional<TimeTablePerTime> t5 = courseOfDay.stream().filter(f -> f.getNo() == 5).findFirst();
                    Optional<TimeTablePerTime> t6 = courseOfDay.stream().filter(f -> f.getNo() == 6).findFirst();
                    // 天气，延迟一秒
//                Map<String, String> weather = WeatherUtils.getJiuJiangTodayWeather();
                    // 早
                    if (t1.isPresent() || t2.isPresent()) {
                        String tableText = createTableText(t1, t2, timeTableUrl);
                        appendInfo(amCourse, tableText, userRobot);
                    }
                    // 中
                    if (t3.isPresent() || t4.isPresent()) {
                        String tableText = createTableText(t3, t4, timeTableUrl);
                        appendInfo(pmCourse, tableText, userRobot);
                    }
                    // 晚
                    if (t5.isPresent() || t6.isPresent()) {
                        String tableText = createTableText(t5, t6, timeTableUrl);
                        appendInfo(eveCourse, tableText, userRobot);
                    }
                }
            }
            sendList(amCourse, am.toSecondOfDay() - TimeTableUtils.firstTime);
            sendList(pmCourse, pm.toSecondOfDay() - TimeTableUtils.firstTime);
            sendList(eveCourse, ni.toSecondOfDay() - TimeTableUtils.firstTime);
            log.info("今日任务创建成功,日期:{}", today);
        }, exetime, TimeTableUtils.oneDaySeconds, TimeUnit.SECONDS);
    }

    /**
     * 开始(单个号码）
     *
     * @param num          号码(qq号/群号)
     * @param type         类型(qq/群)
     * @param timeTableUrl 课表url
     * @param robotId      机器人id
     */
    public void startByOne(int num, QQType type, String timeTableUrl, int robotId) {

        JvtcUser jvtcUser = JvtcLoginUtils.getJvtcUser();
        if (jvtcUser == null) {
            return;
        }

        List<ScheduledFuture> scheduledFutureList = new ArrayList<>(3);

        // 每天执行
        int now = LocalTime.now().toSecondOfDay();
        int exetime = TimeTableUtils.firstTime - now;
        // 如果超时则第二天执行
        exetime = exetime < 0 ? exetime + TimeTableUtils.oneDaySeconds : exetime;

        log.info("用户[{}]课程提醒已开启!,首次任务将在 {} 后执行", jvtcUser.getUsername(), LocalTime.ofSecondOfDay(exetime).format(DateTimeFormatter.ofPattern("HH小时mm分")));

        // 每天执行一次
        TimeTableUtils.task.scheduleAtFixedRate(() -> {
            // 今天
            String today = LocalDate.now().format(yyyyMMdd);
            // 获取当天课程表字符串(一个星期)
            Html timeTable = JvtcLoginUtils.getTimeTable(today);
            // 转成对象(一天)
            List<TimeTablePerTime> courseOfDay = htmlToBeen(timeTable, LocalDate.now().getDayOfWeek().getValue());

            // 获取课程表
            if (courseOfDay.size() > 0) {

                Optional<TimeTablePerTime> t1 = courseOfDay.stream().filter(f -> f.getNo() == 1).findFirst();
                Optional<TimeTablePerTime> t2 = courseOfDay.stream().filter(f -> f.getNo() == 2).findFirst();
                Optional<TimeTablePerTime> t3 = courseOfDay.stream().filter(f -> f.getNo() == 3).findFirst();
                Optional<TimeTablePerTime> t4 = courseOfDay.stream().filter(f -> f.getNo() == 4).findFirst();
                Optional<TimeTablePerTime> t5 = courseOfDay.stream().filter(f -> f.getNo() == 5).findFirst();
                Optional<TimeTablePerTime> t6 = courseOfDay.stream().filter(f -> f.getNo() == 6).findFirst();
                // 天气，延迟一秒
                //Map<String, String> weather = WeatherUtils.getJiuJiangTodayWeather();
                // 早
                if (t1.isPresent() || t2.isPresent()) {
                    ScheduledFuture scheduled = send(t1, t2, num, type, am.toSecondOfDay() - TimeTableUtils.firstTime, timeTableUrl);
                    scheduledFutureList.add(scheduled);
                }
                // 中
                if (t3.isPresent() || t4.isPresent()) {
                    ScheduledFuture scheduled = send(t3, t4, num, type, pm.toSecondOfDay() - TimeTableUtils.firstTime, timeTableUrl);
                    scheduledFutureList.add(scheduled);
                }
                // 晚
                if (t5.isPresent() || t6.isPresent()) {
                    ScheduledFuture scheduled = send(t5, t6, num, type, ni.toSecondOfDay() - TimeTableUtils.firstTime, timeTableUrl);
                    scheduledFutureList.add(scheduled);
                }
                scheduleds.put(robotId, scheduledFutureList);
                log.info("用户[{}]今日任务创建成功,日期:{}", jvtcUser.getUsername(), today);
            } else {
                log.info("用户[{}]今日没课...,日期:{}", jvtcUser.getUsername(), today);
            }
        }, exetime, TimeTableUtils.oneDaySeconds, TimeUnit.SECONDS);
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

    /**
     * 分组发送
     *
     * @param o1           第一节课
     * @param o2           第二节课
     * @param num          需要发送的对象的号码
     * @param type         发送的对象的类型
     * @param time         发送时间
     * @param timeTableUrl 课表url
     * @return
     */
    public ScheduledFuture send(Optional<TimeTablePerTime> o1, Optional<TimeTablePerTime> o2, int num, QQType type, int time, String timeTableUrl) {
        String tableText = createTableText(o1, o2, timeTableUrl);
        return TimeTableUtils.createSchedule(num, type, tableText, time);
    }

    /**
     * 多重发送
     *
     * @param messageInfos 用户组
     * @param time         延迟的时间
     * @return
     */
    public ScheduledFuture sendList(List<MessageInfo> messageInfos, int time) {
        return TimeTableUtils.createScheduleList(messageInfos, time);
    }

    /**
     * 创建表文本
     *
     * @param o1
     * @param o2
     * @param timeTableUrl
     * @return
     */
    private String createTableText(Optional<TimeTablePerTime> o1, Optional<TimeTablePerTime> o2, String timeTableUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append(line);
        appendTextProcess(o1, sb);
        if (o1.isPresent() && o2.isPresent()) {
            sb.append(line);
        }
        appendTextProcess(o2, sb);
        sb.append(line);
        sb.append("课表:").append(timeTableUrl);
        return sb.toString();
    }

    /**
     * html字符串转对象
     *
     * @param html 字符串
     * @param week 星期几
     * @return
     */
    private List<TimeTablePerTime> htmlToBeen(Html html, int week) {
        List<TimeTablePerTime> timeTables = new ArrayList<>();
        Document doc = html.getDocument();
        // 获取单元格
        Elements table_td = doc.select("table").select("td:nth-child(" + (week + 1) + ")");
        for (int i = 0; i < table_td.size(); i++) {
            Element element_td = table_td.get(i);
            if (!StringUtils.isEmpty(element_td.html())) {
                String title = element_td.select("p").attr("title");
                TimeTablePerTime timeTablePerTime = new TimeTablePerTime();
                if (!StringUtils.isEmpty(title)) {
                    String[] info = title.split("<br/>");
                    timeTablePerTime.setNo(i + 1);
                    timeTablePerTime.setCourseName(info[2]);
                    timeTablePerTime.setUpTime("上课时间：" + time[i].format(hhmm));
                    timeTablePerTime.setAddress(info[4]);
                    timeTables.add(timeTablePerTime);
                }
            }
        }
        return timeTables;
    }

    /**
     * 文本处理
     *
     * @param optional
     * @param sb
     * @return
     */
    private void appendTextProcess(Optional<TimeTablePerTime> optional, StringBuilder sb) {
        if (optional.isPresent()) {
            TimeTablePerTime timeTablePerTime = optional.get();
            int no = timeTablePerTime.getNo();
            String courseName = timeTablePerTime.getCourseName();
            String address = timeTablePerTime.getAddress();
            String upTimeWeekStr = timeTablePerTime.getUpTime();
            String upTimeStr = time[timeTablePerTime.getNo() - 1].format(hhmm);
            appendText(sb, no, courseName, address, upTimeWeekStr, upTimeStr);
        }
    }

    /**
     * 文本补充
     *
     * @param sb
     * @param no
     * @param courseName
     * @param address
     * @param upTimeWeekStr
     * @param upTimeStr
     */
    private void appendText(StringBuilder sb, int no, String courseName, String address, String upTimeWeekStr, String upTimeStr) {
        sb.append("第" + no + "节课\n");
        sb.append(courseName + "\n");
        sb.append(address + "\n");
        sb.append(upTimeWeekStr + "\n");
    }

}