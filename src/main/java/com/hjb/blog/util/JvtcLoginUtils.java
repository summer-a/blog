package com.hjb.blog.util;

import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.entity.vo.ResponseVO;
import com.hjb.blog.service.common.RedisService;
import com.hjb.blog.service.normal.JvtcUserService;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.selector.Html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jvtc登录工具
 *
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcLoginUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/6/26 19:38
 */
public class JvtcLoginUtils {

    /**
     * 处理页面<br>
     * 参数rq=日期
     */
    private static String kbPage = "http://jiaowu.jvtc.jx.cn/jsxsd/framework/main_index_loadkb.jsp";

    /**
     * 登录连接
     */
    private static String loginUrl = "http://jiaowu.jvtc.jx.cn/jsxsd/xk/LoginToXk";

    /**
     * 主页，包含用户信息
     */
    private static String infoPage = "http://jiaowu.jvtc.jx.cn/jsxsd/framework/xsMain_new.jsp";

    private static Pattern pattern = Pattern.compile("第([0-9]+)周");

    /**
     * 配置
     */
    private static Request.Builder request = initParam();

    public static final String COOKIE = "Cookie";

    public static final String SET_COOKIE = "Set-Cookie";

    public static final String LOGIN_TITLE_TEXT = "登录";


    private JvtcLoginUtils() {
    }

    /**
     * 获取用户
     *
     * @param request
     * @return
     */
    public static JvtcUser getJvtcUser(HttpServletRequest request) {
        return (JvtcUser) request.getSession().getAttribute("jvtc_user");
    }

    /**
     * 获取当前会话的用户
     *
     * @return
     */
    public static JvtcUser getJvtcUser() {
        HttpSession currentSession = SpringUtils.getCurrentSession();
        if (currentSession != null) {
            return (JvtcUser) currentSession.getAttribute("jvtc_user");
        }
        return null;
    }


    /**
     * 获取课程表
     *
     * @param howWeeks 当前第几周(绝对周)
     * @param jvtcUser 用户
     * @param retryCount 重试次数
     * @return
     */
    public static Html getTimeTable(Integer howWeeks, JvtcUser jvtcUser, int retryCount) {

        // 当前第几周
        String url = kbPage + "?rq=" + LocalDate.now().plusWeeks(howWeeks == null ? 0 : howWeeks - howWeeks(LocalDate.now())).format(DateTimeFormatter.ISO_LOCAL_DATE);
        // 有缓存就根据缓存获取
        if (!StringUtils.isEmpty(jvtcUser.getCookie())) {

            // 根据班级获取该班级的课表（本周）
            RedisService<String> redisService = SpringUtils.getBean(RedisService.class);
            // 0表示当前周, 要进行设置
            howWeeks = howWeeks == 0 ? howWeeks(LocalDate.now()) : howWeeks;
            String table = redisService.get(String.format("table::%s::%s", jvtcUser.getUsername(), howWeeks));
            if (!StringUtils.isEmpty(table)) {
                return Html.create(table);
            }
            request.header(COOKIE, jvtcUser.getCookie());
            // 判断缓存是否过期
            ResponseVO kbPageResponse = HttpUtils.get(url, request);
            Html kbHtml = Html.create(kbPageResponse.getHtml());
            // 成功
            if (isLogined(kbHtml)) {
                return processAndSaveToRedis(jvtcUser.getUsername(), howWeeks, kbHtml);
            }

        }
        // 未登录则进行登录,根据缓存获取页面失败了，表示缓存过期了
        ResponseVO loginResult = loginByUserNameAndEncode(jvtcUser.getUsername(), jvtcUser.getPassword());
        if (loginResult != null && loginResult.getCode() == HttpStatus.FOUND.value()) {
            request.header(COOKIE, loginResult.getHeaders().get(SET_COOKIE));
        }

        // 获取课表
        ResponseVO result = HttpUtils.get(url, request);
        Html html = Html.create(result.getHtml());
        if (result.getCode() == HttpStatus.OK.value() && isLogined(html)) {
            return processAndSaveToRedis(jvtcUser.getUsername(), howWeeks, html);
        }

        if (retryCount >= 1) {
            return getTimeTable(howWeeks, jvtcUser, --retryCount);
        }

        return Html.create("<div>获取失败,请刷新重试</div>");
    }

    private static Html processAndSaveToRedis(String id, Integer weeks, Html html) {
        // 处理
        Html newHtml = processHtml(id, weeks, html);
        // 存入redis
        saveToRedis(id, weeks, newHtml.get());
        return newHtml;
    }
    /**
     * html页面处理<br>@
     * 将源页面修改为通用页面
     *
     * @param weeks 第几周(绝对周)
     * @param html  代码体
     */
    public static Html processHtml(String id, Integer weeks, Html html) {

        try {
            Document doc = html.getDocument();
            doc.title(String.format("第 %s 周", weeks));
            doc.select("table")
                    .attr("border", "1")
                    .attr("cellspacing", "0")
                    .attr("cellpadding", "0")
                    .removeAttr("style")
                    .removeAttr("class")
                    // 将td补全
                    .select("td").select("p")
                    .forEach(element -> {
                        element.html(element.attr("title"));
                    });
            doc.getElementsByTag("table").get(0).before(createPrevOrNextWeekNode("/", weeks, id));
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return html;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return Html.create("");
    }

    /**
     * 保存到redis
     *
     * @param id    学号（根据学号存入表格，格式[table::学号::相对几周])
     * @param weeks 相对几周
     * @param html  html代码
     */
    public static void saveToRedis(String id, Integer weeks, String html) {
        // 插入redis
        if (!StringUtils.isEmpty(id)) {
            RedisService<String> redisService = SpringUtils.getBean(RedisService.class);
            // 存入前清除\t\r\n
            redisService.set(String.format("table::%s::%s", id, weeks == null ? 0 : weeks), html.replaceAll("[\t|\r|\n]", ""), 1 * 24 * 60 * 60);
        }
    }

    /**
     * 进行登录
     *
     * @param username 用户名
     * @param encoded  压缩的密码
     * @return 如果返回401表示账号密码有误
     */
    public static ResponseVO loginByUserNameAndEncode(String username, String encoded) {

        JvtcUserService jvtcUserService = SpringUtils.getBean(JvtcUserService.class);

        // 非缓存登录(设置禁用临时重定向后一次就行)
        ResponseVO jvtcResponse = loginRequest(loginUrl, username, encoded);
        String cookie = jvtcResponse.getHeaders().get(SET_COOKIE);
        if (StringUtils.isEmpty(cookie)) {
            return null;
        }

        // 清除cookie后其他内容
        cookie = cookie.split(";")[0];
        jvtcResponse.setCookie(cookie);

        // 插入或更新数据库缓存
        JvtcUser user = new JvtcUser();
        user.setUsername(username);
        JvtcUser jvtcUser = jvtcUserService.selectOne(user);

        if (jvtcResponse != null &&
                (jvtcResponse.getCode() == HttpStatus.OK.value()
                        || jvtcResponse.getCode() == HttpStatus.FOUND.value()
                )) {
            // 插入/更新cookie
            if (jvtcUser == null) {
                jvtcUser = new JvtcUser();
                jvtcUser.setUsername(username);
                jvtcUser.setPassword(encoded);
                jvtcUser.setCookie(cookie);
                jvtcUserService.insertSelective(jvtcUser);
            } else {
                // 缓存不一致则更新
                if (!jvtcUser.getCookie().equals(cookie)) {
                    JvtcUser juser = new JvtcUser();
                    juser.setId(jvtcUser.getId());
                    juser.setCookie(cookie);
                    jvtcUserService.update(juser);
                }
                // 更新班级
                if (StringUtils.isEmpty(jvtcUser.getClazz())) {
                    JvtcUser juser = new JvtcUser();
                    juser.setId(jvtcUser.getId());

                    Request.Builder builder = JvtcLoginUtils.initParam();
                    builder.addHeader(COOKIE, cookie);
                    ResponseVO userInfo = HttpUtils.get(infoPage, builder);
                    Html userInfoHtml = Html.create(userInfo.getHtml());
                    juser.setClazz(userInfoHtml.xpath("//div[@class='middletopttxlr']/div[6]/div[2]/text()").get());
                    jvtcUserService.update(juser);
                }
            }

            return jvtcResponse;
        }
        return new ResponseVO(401, null, null);
    }

    /**
     * 登录状态
     *
     * @param html 登录后的页面
     * @return
     */
    public static boolean isLogined(Html html) {
        if (html == null) {
            return false;
        }
        if (Objects.equals(html.getDocument().title(), LOGIN_TITLE_TEXT)) {
            return false;
        } else if (StringUtils.isEmpty(html.getDocument().tagName("tbody").text().trim())) {
            return false;
        }
        return true;
    }

    /**
     * 登录状态
     *
     * @param cookie 缓存
     * @return
     */
    public static boolean isLogined(String cookie) {
        Request.Builder builder = initParam().addHeader(COOKIE, cookie);
        ResponseVO resp = HttpUtils.get(kbPage, builder);
        return isLogined(Html.create(resp.getHtml()));
    }

    /**
     * 进行登录请求
     *
     * @param username 用户名
     * @param encoded  加密数据
     * @return
     */
    public static ResponseVO loginRequest(String username, String encoded) {
        return loginRequest(loginUrl, username, encoded, null);
    }

    /**
     * 进行登录请求
     *
     * @param url      登录url
     * @param username 用户名
     * @param encoded  加密数据
     * @return
     */
    public static ResponseVO loginRequest(String url, String username, String encoded) {
        return loginRequest(url, username, encoded, null);
    }

    /**
     * 进行登录请求
     *
     * @param url      登录url
     * @param username 用户名
     * @param encoded  加密数据
     * @return
     * @parma cookie 缓存
     */
    public static ResponseVO loginRequest(String url, String username, String encoded, String cookie) {

        RequestBody body = new FormBody.Builder()
                .add("userAccount", username)
                .add("userPassword", "")
                .add("encoded", encoded)
                .build();
        Request.Builder builder = initParam();
        // 缓存存在则直接缓存登录
        if (!StringUtils.isEmpty(cookie)) {
            builder.addHeader(COOKIE, cookie);
        }

        return HttpUtils.post(url, builder, body);
    }


    /**
     * 参数初始化
     *
     * @return
     */
    private static Request.Builder initParam() {
        return new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Referer", "http://jiaowu.jvtc.jx.cn/jsxsd/")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Host", "jiaowu.jvtc.jx.cn")
                .addHeader("Origin", "http://jiaowu.jvtc.jx.cn");
    }

    /**
     * 创建周期切换节点
     *
     * @param host  主机地址
     * @param weeks 多少周
     * @param id    教务系统用户id
     * @return
     */
    private static String createPrevOrNextWeekNode(String host, int weeks, String id) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style='width: 100%;height: 70px;line-height: 70px;text-align: center;padding: 5px 0px;'>");
        sb.append("	<a href='" + host + "timetable/" + id + "/" + (weeks - 1) + "' style='text-decoration: none;font-size:35px;float: left;padding-left: 10px;padding-right: 5px;border-right: 3px solid burlywood;width:40%;border-top: 3px solid burlywood;' target='_parent'>上一周</a>");
        sb.append(" <span style='font-size:35px;'><b>" + String.format("第 %s 周", weeks) + "</b></span>");
        sb.append("	<a href='" + host + "timetable/" + id + "/" + (weeks + 1) + "' style='text-decoration: none;font-size:35px;float: right;padding-right: 10px;padding-left: 5px;border-left: 3px solid burlywood;width:40%;border-top: 3px solid burlywood;' target='_parent'>下一周</a>");
        sb.append("</div>");
        return sb.toString();
    }

    /**
     * 开学多少周
     *
     * @return
     */
    public static int howWeeks(LocalDate now) {
        int week = now.getDayOfWeek().getValue();
        // 计算开学到现在多少周（减掉开学之前的时间）
        return now.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear()) - ((week == 6 || week == 7) ? 34 : 35);
    }

    /**
     * 开学多少周
     *
     * @param html
     * @return
     */
    public static String howWeeks(Html html) {
        List<String> timeTable = html.xpath("//p/@title").all();
        if (timeTable != null) {
            for (String table : timeTable) {
                if (!StringUtils.isEmpty(table)) {
                    Matcher matcher = pattern.matcher(table);
                    if (matcher.find()) {
                        return matcher.group();
                    }
                }
            }
        }
        return "第 ? 周";
    }

}
