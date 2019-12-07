package com.hjb.blog;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.dto.ArticleSearchDTO;
import com.hjb.blog.entity.dto.UserRobotDTO;
import com.hjb.blog.entity.enums.QQType;
import com.hjb.blog.entity.jvtc.JvtcCourse;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.normal.Robot;
import com.hjb.blog.entity.timetable.MessageInfo;
import com.hjb.blog.quartz.SendMessageJob;
import com.hjb.blog.service.common.CourseService;
import com.hjb.blog.service.common.ElasticsearchService;
import com.hjb.blog.service.common.RedisService;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.JvtcUserService;
import com.hjb.blog.util.CoolqUtils;
import com.hjb.blog.util.JvtcLoginUtils;
import com.hjb.blog.util.ReprintUtils;
import com.hjb.blog.util.TimeTableUtils;
import com.xiaoleilu.hutool.util.CollectionUtil;
import okhttp3.*;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selector;
import us.codecraft.webmagic.selector.XpathSelector;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
@Transactional
@Rollback
public class BlogApplicationTests {

    @Resource
    private JvtcUserService jvtcUserService;

    @Resource
    private ArticleService articleService;

    @Resource
    private RestHighLevelClient client;

    @Resource
    private RedisService redisService;

    @Resource
    private SendMessageJob sendMessageJob;

    @Resource
    private CourseService courseService;

    @Resource
    private ElasticsearchService<ArticleSearchDTO> elasticsearchService;

    public static void main(String[] args) {
        Html html = new Html("<table class='h'><div> f  </div></table>");
        Selector s = new XpathSelector("//div[0]");
        String s1 = html.select(s).get();
        String div = html.getDocument().tagName("div").text();
        System.out.println(s1);
        System.out.println(div);
    }

    @Test
    public void test() {
        String url = "http://jiaowu.jvtc.jx.cn/jsxsd/framework/main_index_loadkb.jsp?rq=2019-12-05";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().build();
        Request.Builder builder = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Referer", "http://jiaowu.jvtc.jx.cn/jsxsd/")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Host", "jiaowu.jvtc.jx.cn")
                .addHeader("Origin", "http://jiaowu.jvtc.jx.cn")
                .addHeader("Cookie", "JSESSIONID=7E3A0F42AF7D149AD4969D517CB16118");
        Request request = builder.url(url).post(body).build();
        try {
            Response execute = client.newCall(request).execute();
            if (execute.isSuccessful()) {
                System.out.println(execute.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTimeTable() {
        while (true) {
            sendMessageJob.insertToRedis();
        }
    }

    @Test
    public void redisDemo() {
        Object o = redisService.get("table::183051406");
        System.out.println(o);
    }

    @Test
    public void sendMsg() {
        CoolqUtils.getInstance().sendPrivateMsg(1525947163, "nihao");
    }

    @Test
    public void getCourse() {
        send(3, true);
    }

    private void send(int interval, boolean isFifthLesson) {
        List<UserRobotDTO> userRobots = jvtcUserService.selectUserRobotList();
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (UserRobotDTO userRobot : userRobots) {
            JvtcUser jvtcUser = userRobot.getJvtcUser();
            // 记得去掉
            jvtcUser.setPassword("hjb19970325");
            List<JvtcCourse> courses = courseService.selectCourseIfSignedOrNot(jvtcUser, LocalDate.now().format(DateTimeFormatter.ISO_DATE), 3);

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
                        //String tableText = createTableText(t1, t2, timeTableUrl);
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

    @Test
    public void getTimeTable() {
        JvtcUser user = new JvtcUser();
        user.setId(1);
        JvtcUser jvtcUser = jvtcUserService.selectOne(user);
        Html timeTable = JvtcLoginUtils.getTimeTable(0, jvtcUser, 3);
        System.out.println(timeTable);
    }

    @Test
    public void csdnReprint() {
        List<Article> reprintArticles = ReprintUtils.csdnList("java");
        System.out.println(reprintArticles);

        ReprintUtils.csdnReprint(reprintArticles);
    }

    @Test
    public void search() {
        String keyword = "Java";
        PageInfo<Map<String, Object>> search1 =
                elasticsearchService.search(0, 10, "article", "document", "title", "id,title", null, keyword);
        System.out.println("s1=" + JSON.toJSONString(search1));

        PageInfo<Map<String, Object>> search2 = elasticsearchService.search(0, 10, "article", "document", "title,content,date", "id,title,content,summary,viewCount,likeCount,commentCount,date", null, keyword);
        System.out.println("s2=" + JSON.toJSONString(search2));
    }

    @Test
    public void contextLoads() {

        List<Article> articleList = articleService.selectListForFullArticle(new Article());

        articleList.forEach(r -> {
            ArticleSearchDTO info = new ArticleSearchDTO();
            info.setId(r.getId().longValue());
            info.setTitle(r.getArticleTitle());
            info.setContent(r.getArticleContent());
            info.setCommentCount(r.getArticleCommentCount().longValue());
            info.setLikeCount(r.getArticleLikeCount().longValue());
            info.setViewCount(r.getArticleViewCount().longValue());
            info.setSummary(r.getArticleSummary());
            info.setDate(r.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd号")));
        });
        System.out.println("ok");

        GetRequest getRequest = new GetRequest();
        try {
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(documentFields);
        } catch (IOException e) {e.printStackTrace();

        }
    }

}
