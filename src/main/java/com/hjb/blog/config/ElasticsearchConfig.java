package com.hjb.blog.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: ElasticsearchConfig
 * @projectName blog
 * @description: TODO
 * @date 2019/8/12 19:07
 */
@Configuration
public class ElasticsearchConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);

    /**
     * 集群地址，多个用,隔开
     */
    @Value("${host.ip}")
    private String hosts;
    /**
     * 使用的端口号
     */
    @Value("${host.es.rest-port}")
    private int port;
    /**
     * 使用的协议
     */
    @Value("${host.es.schema}")
    private String schema;

    /**
     * 连接超时时间
     */
    private int connectTimeOut = 1000;
    /**
     * 连接超时时间
     */
    private int socketTimeOut = 30000;
    /**
     * 获取连接的超时时间
     */
    private int connectionRequestTimeOut = 500;
    /**
     * 最大连接数
     */
    private int maxConnectNum = 100;
    /**
     * 最大路由连接数
     */
    private int maxConnectPerRoute = 100;

    @Bean
    public RestHighLevelClient client() {
        LOGGER.info("Es配置...");
        // 封装
        List<HttpHost> hostList = Arrays.asList(hosts.split(",")).stream().map(r -> new HttpHost(r, port, schema)).collect(Collectors.toList());
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }


}
