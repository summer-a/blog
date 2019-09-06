package com.hjb.blog.util;

import com.hjb.blog.entity.vo.ResponseVO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: HttpUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/8/31 14:26
 */
public class HttpUtils {

    /**
     * okhttp客户端
     */
    private static OkHttpClient client = new OkHttpClient();

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static ResponseVO get(String url, Request.Builder builder) {
        // 请求
        Request request = builder.url(url).build();

        // 响应
        try (Response response = client.newCall(request).execute()) {
            return new ResponseVO(response.code(), Html.create(response.body().string()), response.headers());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseVO(500, null, null);
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
    public static ResponseVO post(String url, Request.Builder builder, RequestBody body) {
        Request request = builder.url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return new ResponseVO(response.code(), Html.create(response.body().string()), response.headers());
        } catch (IOException e) {
            return null;
        }
    }
}
