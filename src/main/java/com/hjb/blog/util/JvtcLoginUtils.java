package com.hjb.blog.util;

import com.hjb.blog.entity.dto.JvtcResponse;
import com.hjb.blog.entity.normal.JvtcUser;
import com.hjb.blog.service.normal.JvtcUserService;
import okhttp3.*;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import us.codecraft.webmagic.selector.Html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

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

    /**
     * 格式化器
     */
    private static DateTimeFormatter formatYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * okhttp客户端
     */
    private static OkHttpClient client = new OkHttpClient();

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
     * @param request
     * @return
     */
    public static JvtcUser getJvtcUser(HttpServletRequest request) {
        return (JvtcUser) request.getSession().getAttribute("jvtc_user");
    }

    /**
     * 获取当前会话的用户
     * @return
     */
    public static JvtcUser getJvtcUser() {
        HttpSession currentSession = getCurrentSession();
        if (currentSession != null) {
            return (JvtcUser) currentSession.getAttribute("jvtc_user");
        }
        return null;
    }

    /**
     * 获取当前会话
     * @return
     */
    public static HttpSession getCurrentSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            return request.getSession();
        }
        return null;
    }

    /**
     * 获取课程表<br>
     *     该方法必须在已登录的状态下使用
     * @param date
     * @return
     */
    public static Html getTimeTable(String date) {
        JvtcUser jvtcUser = JvtcLoginUtils.getJvtcUser();
        if (jvtcUser != null) {
            return getTimeTable(date, jvtcUser.getUsername(), jvtcUser.getPassword(), jvtcUser.getCookie());
        }
        return null;
    }

    /**
     * 获取课程表
     *
     * @param date            日期
     * @param username        用户名
     * @param encoded         编码过的密码
     * @param cookie          缓存
     * @param jvtcUserService 用户服务，更新缓存
     * @return
     */
    public static Html getTimeTable(String date, String username, String encoded, String cookie) {

        // 有缓存就根据缓存获取
        if (!StringUtils.isEmpty(cookie)) {

            request.addHeader(COOKIE, cookie);
            // 判断缓存是否过期
            JvtcResponse kbPageResponse = get(kbPage + "?rq=" + date, request);
            Html kbHtml = kbPageResponse.getHtml();
            // 成功
            if (isLogined(kbHtml)) {
                return kbHtml;
            }

        }
        // 未登录则进行登录,根据缓存获取页面失败了，表示缓存过期了
        loginByUserNameAndEncode(username, encoded);


        // 获取日期
        LocalDate now = null;

        // 根据条件获取
        if (StringUtils.isEmpty(date)) {
            now = LocalDate.now();
        } else {
            try {
                now = LocalDate.parse(date);
            } catch (Exception e) {
                try {
                    // 可以根据数字获取相对现在的第N周
                    int weeks = Integer.parseInt(date);
                    now = LocalDate.now().plusWeeks(weeks);
                } catch (Exception e2) {
                    now = LocalDate.now();
                }
            }
        }
        // 是否是周末
        boolean isWeek = false;
        // 格式化日期为字符串
        date = now.format(formatYMD);
        // 周末就直接显示下一周课表
        if (now.getDayOfWeek().getValue() == 6 || now.getDayOfWeek().getValue() == 7) {
            date = now.plusWeeks(1).format(formatYMD);
            isWeek = true;
        }
        // 获取课表
        JvtcResponse result = get(kbPage + "?rq=" + date, request);

        if (result.getCode() == 200 && !StringUtils.isEmpty(result.getHtml())) {

            Document doc = result.getHtml().getDocument();
            // 计算开学到现在多少周（减掉开学之前的时间）
            String weekTitle = "第 " + (now.get(ChronoField.ALIGNED_WEEK_OF_YEAR) - (isWeek ? 6 : 7)) + " 周";
            doc.title(weekTitle);
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
            doc.getElementsByTag("table").get(0).before(createPrevOrNextWeekNode(now, "/", weekTitle));

            return result.getHtml();
        }

        return null;
    }


    /**
     * 进行登录
     *
     * @param username        用户名
     * @param encoded         压缩的密码
     * @return 如果返回401表示账号密码有误
     */
    public static JvtcResponse loginByUserNameAndEncode(String username, String encoded) {

        JvtcUserService jvtcUserService = SpringUtils.getBean(JvtcUserService.class);

        // 第一遍非缓存登录
        JvtcResponse jvtcResponse = loginRequest(loginUrl, username, encoded);
        String cookie = jvtcResponse.getHeaders().get(SET_COOKIE);
        if (StringUtils.isEmpty(cookie)) {
            return null;
        }

        // 清除cookie后其他内容
        cookie = cookie.split(";")[0];

        // 第二遍缓存登录
        JvtcResponse loginResponse = loginRequest(loginUrl, username, encoded, cookie.split(";")[0]);
        // 插入或更新数据库缓存
        JvtcUser user = new JvtcUser();
        user.setUsername(username);
        JvtcUser jvtcUser = jvtcUserService.selectOne(user);

        if (isLogined(loginResponse.getHtml())) {
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
                    JvtcResponse userInfo = JvtcLoginUtils.get(infoPage, builder);
                    Html userInfoHtml = userInfo.getHtml();
                    juser.setClazz(userInfoHtml.xpath("//div[@class='middletopttxlr']/div[6]/div[2]/text()").get());
                    jvtcUserService.update(juser);
                }
            }

            return loginResponse;
        }
        return new JvtcResponse(401, null, null);
    }

    /**
     * 登录状态
     *
     * @param html 登录后的页面
     * @return
     */
    public static boolean isLogined(Html html) {
        if (html.getDocument().title().equals(LOGIN_TITLE_TEXT)) {
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
        JvtcResponse resp = get(kbPage, builder);
        return isLogined(resp.getHtml());
    }

    /**
     * 进行登录请求
     *
     * @param username 用户名
     * @param encoded  加密数据
     * @return
     */
    public static JvtcResponse loginRequest(String username, String encoded) {
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
    public static JvtcResponse loginRequest(String url, String username, String encoded) {
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
    public static JvtcResponse loginRequest(String url, String username, String encoded, String cookie) {

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

        return post(url, builder, body);
    }


    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static JvtcResponse get(String url, Request.Builder builder) {
        // 请求
        Request request = builder.url(url).build();

        // 响应
        try (Response response = client.newCall(request).execute()) {
            return new JvtcResponse(response.code(), Html.create(response.body().string()), response.headers());
        } catch (IOException e) {
            e.printStackTrace();
            return new JvtcResponse(500, null, null);
        }
    }

    /**
     * post 请求
     *
     * @param url     地址
     * @param builder 信息
     * @param body    主体信息
     * @return
     * @throws IOException
     */
    private static JvtcResponse post(String url, Request.Builder builder, RequestBody body) {
        Request request = builder.url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return new JvtcResponse(response.code(), Html.create(response.body().string()), response.headers());
        } catch (IOException e) {
            return null;
        }
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
                .addHeader("Origin", "http://jiaowu.jvtc.jx.cn")
                .addHeader("Cache-Control", "max-age=0");
    }

    /**
     * 创建周期切换节点
     *
     * @param date 日期
     * @param host 主机地址
     * @param week 周标题
     * @return
     */
    private static String createPrevOrNextWeekNode(LocalDate date, String host, String week) {
        StringBuilder sb = new StringBuilder();
        // 上一周
        String prev = date.minusWeeks(1).format(formatYMD);
        // 下一周
        String next = date.plusWeeks(1).format(formatYMD);
        sb.append("<div style=\"width: 100%;height: 70px;line-height: 70px;text-align: center;padding: 5px 0px;\">");
        sb.append("	<a href=\"" + (host + "?date=" + prev) + "\" style=\"text-decoration: none;font-size:35px;float: left;padding-left: 10px;padding-right: 5px;border-right: 3px solid burlywood;width:40%;border-top: 3px solid burlywood;\">上一周</a>");
        sb.append(" <span style=\"font-size:35px;\"><b>" + week + "</b></span>");
        sb.append("	<a href=\"" + (host + "?date=" + next) + "\" style=\"text-decoration: none;font-size:35px;float: right;padding-right: 10px;padding-left: 5px;border-left: 3px solid burlywood;width:40%;border-top: 3px solid burlywood;\">下一周</a>");
        sb.append("</div>");
        return sb.toString();
    }

}
