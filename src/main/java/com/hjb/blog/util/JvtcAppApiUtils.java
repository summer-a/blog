package com.hjb.blog.util;

import com.hjb.blog.entity.vo.ResponseVO;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 教务处App的Api工具
 *
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcAppUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/11/21 0:24
 */
public class JvtcAppApiUtils {

    private static Logger logger = Logger.getLogger(JvtcAppApiUtils.class);

    private static final String API = "http://jiaowu.jvtc.jx.cn/app.do";

    /**
     * 用户认证<br>
     * html响应结构如下<br>
     * {<br>
     * "success": true,<br>
     * "token": "",<br>
     * "user": {<br>
     * "userdwmc": "信息工程学院",<br>
     * "scsj": ,<br>
     * "sjyzm": ,<br>
     * "usertype": ,<br>
     * "useraccount": ,<br>
     * "userpasswd": ,<br>
     * "username": <br>
     * },<br>
     * "usertype": ,<br>
     * "userrealname": ,<br>
     * "userdwmc": <br>
     * }
     *
     * @param account  账户
     * @param password 密码
     * @return
     */
    public static JSONObject authUser(String account, String password) {
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("method", "authUser")
                .addEncoded("xh", account)
                .addEncoded("pwd", password)
                .build();
        Request.Builder defaultRequest = getDefaultRequest();
        return HttpUtils.postReturnJson(API, defaultRequest, formBody);
    }

    /**
     * 获取用户信息<br>
     * html响应结构<br>
     * {<br>
     * "xxdm": "",<br>
     * "yxid": "",<br>
     * "bjid": "",<br>
     * "ndzyid": ""<br>
     * }
     *
     * @param account
     * @param token
     * @return
     */
    public static JSONObject getUserInfo(String account, String token) {
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("method", "getStudentIdInfo")
                .addEncoded("xh", account)
                .add("usertype", "2")
                .build();
        Request.Builder defaultRequest = getDefaultRequest()
                .addHeader("token", token);
        return HttpUtils.postReturnJson(API, defaultRequest, formBody);
    }

    /**
     * 获取当前服务器时间(获取课表)<br>
     * html响应结构<br>
     * {<br>
     * "s_time": "起始时间",<br>
     * "xnxqh": "学期",<br>
     * "zc": 周次,<br>
     * "e_time": "结束时间"<br>
     * }
     *
     * @param token
     * @param date  格式yyyy-MM-dd
     * @return
     */
    public static JSONObject getCurrentTime(String token, String date) {
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("method", "getCurrentTime")
                .addEncoded("currDate", date)
                .build();
        Request.Builder defaultRequest = getDefaultRequest()
                .addHeader("token", token);
        return HttpUtils.postReturnJson(API, defaultRequest, formBody);
    }

    /**
     * 获取课表<br>
     * html响应结构(集合)<br>
     * {<br>
     * "jssj": "结束时间(09:45)",<br>
     * "sjbz": "0",<br>
     * "kkzc": "教学周(2-5,7-17)",<br>
     * "kcmc": "课程名称",<br>
     * "kcsj": "课程时间(10102)第一个1表示星期,0102表示第一和第二节课",<br>
     * "kssj": "开始时间(08:05)",<br>
     * "jsmc": "教室名称",<br>
     * "jsxm": "老师名字"<br>
     * }
     *
     * @param token   认证token
     * @param account 账户
     * @param xnxqid  学期id
     * @param zc      周次
     * @return
     */
    public static JSONArray getKcb(String token, String account, String xnxqid, String zc) {
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("method", "getKbcxAzc")
                .addEncoded("xh", account)
                .addEncoded("xnxqid", xnxqid)
                .addEncoded("zc", zc)
                .build();
        Request.Builder defaultRequest = getDefaultRequest()
                .addHeader("token", token);
        ResponseVO result = HttpUtils.post(API, defaultRequest, formBody);
        if (result.getCode() == HttpStatus.OK.value()) {
            return new JSONArray(result.getHtml());
        }
        return new JSONArray();
    }

    /**
     * 获取当前课表<br>
     * html响应结构(集合)<br>
     * {<br>
     * "jssj": "结束时间(09:45)",<br>
     * "sjbz": "0",<br>
     * "kkzc": "教学周(2-5,7-17)",<br>
     * "kcmc": "课程名称",<br>
     * "kcsj": "课程时间(10102)第一个1表示星期,0102表示第一和第二节课",<br>
     * "kssj": "开始时间(08:05)",<br>
     * "jsmc": "教室名称",<br>
     * "jsxm": "老师名字"<br>
     * }
     *
     * @param token   认证token
     * @param account 账户
     * @return
     */
    public static JSONArray getKcb(String token, String account) {
        // 获取服务器时间
        JSONObject currentTime = getCurrentTime(token, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        if (currentTime != null) {
            try {
                return getKcb(token, account, currentTime.getStr("xnxqh"), currentTime.getStr("zc"));
            } catch (Exception e) {
                logger.error("获取课表失败", e);
            }
        }
        return new JSONArray();
    }


    /**
     * 默认初始请求
     */
    private static Request.Builder getDefaultRequest() {
        return new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Mobile; Android 7.0;Redmi Note 4X Build/FRF91 )")
                .addHeader("X-QZ-APP-INFO", "11435727/public")
                .addHeader("x-mas-app-id", "11435752")
                .addHeader("Charset", "UTF-8");
    }

}
