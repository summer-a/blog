package com.hjb.blog.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.base.EsBaseEntity;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.service.common.ElasticsearchService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务实现
 *
 * @author 胡江斌
 * @version 1.0
 * @title: ElasticsearchServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/8/10 8:19
 */
@Service
public class ElasticsearchServiceImpl<T extends EsBaseEntity> implements ElasticsearchService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    @Resource
    private RestHighLevelClient client;

    /**
     * 创建索引
     *
     * @param index 索引名
     * @return
     */
    @Override
    public ResultVO createIndex(String index) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info(JSON.toJSONString(response));
        return ResultVO.ok();
    }

    /**
     * 添加记录
     *
     * @param index 索引名
     * @param type  类型
     * @param t     存入对数
     * @return
     */
    @Override
    public ResultVO add(String index, String type, T t) throws IOException {
        // 索引请求
        IndexRequest request = new IndexRequest(index, type, t.getId().toString());
        // 源
        request.source(JSON.toJSONString(t), XContentType.JSON);
        // 存如
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        LOGGER.info(JSON.toJSONString(response));
        return ResultVO.ok();
    }

    /**
     * 删除记录
     *
     * @param index 索引名
     * @param type  类型
     * @param id    id
     * @return
     */
    @Override
    public ResultVO delete(String index, String type, Long id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, type, id.toString());
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        LOGGER.info(JSON.toJSONString(response));
        return ResultVO.ok();
    }

    /**
     * 更新记录
     *
     * @param index 索引名
     * @param type  类型
     * @param t     记录
     * @return
     */
    @Override
    public ResultVO update(String index, String type, T t) throws IOException {
        UpdateRequest request = new UpdateRequest(index, type, t.getId().toString());
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        LOGGER.info(JSON.toJSONString(response));
        return ResultVO.ok();
    }

    /**
     * 获取单个记录
     *
     * @param index 索引名
     * @param type  类型
     * @param id    id
     * @return
     */
    @Override
    public Map<String, Object> getOne(String index, String type, Long id) throws IOException {
        GetRequest request = new GetRequest(index, type, id.toString());
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        return response == null ? new HashMap<>(0) : response.getSource();
    }

    /**
     * 搜索
     *
     * @param pageNum       起始页(从0开始)
     * @param pageSize      每页数量
     * @param index         索引名
     * @param type          类型
     * @param fields        要查询的字段,多个字段使用,(英文逗号)分隔
     * @param includeFields 获取的字段,多个字段使用,(英文逗号)分隔
     * @param excludeFields 过滤的字段,多个字段使用,(英文逗号)分隔
     * @param keyword       关键词
     * @return
     */
    @Override
    public PageInfo<Map<String, Object>> search(Integer pageNum, Integer pageSize, String index, String type, String fields, String includeFields, String excludeFields, String keyword) {

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(fields)) {
            String[] fieldArray = fields.split(",");
            // matchQuery效率高一丢丢ik_smart粗粒度, ik_max_word细粒度
            if (fieldArray.length == 1) {
                builder.must(QueryBuilders.matchQuery(fields, keyword).analyzer("ik_max_word"));
            } else {
                builder.must(QueryBuilders.multiMatchQuery(keyword, fieldArray).analyzer("ik_max_word"));
            }
        } else {
            builder.must(QueryBuilders.queryStringQuery(keyword).analyzer("ik_max_word"));
        }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        // 没有开始页,就不分页
        if (pageNum != null) {
            sourceBuilder.from(pageNum);
            sourceBuilder.size(pageSize);
        }
        // 第一个字符串数组是获取字段，第二个是过滤的字段，默认获取全部
        if (!StringUtils.isEmpty(includeFields) || !StringUtils.isEmpty(excludeFields)) {
            sourceBuilder.fetchSource(
                    StringUtils.isEmpty(includeFields) ? new String[]{} : includeFields.split(","),
                    StringUtils.isEmpty(excludeFields) ? new String[]{} : excludeFields.split(",")
            );
        }

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();

            SearchHit[] searchHits = hits.getHits();

            List<Map<String, Object>> list = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                Map<String, Object> map = hit.getSourceAsMap();
                list.add(map);
            }
            return PageInfo.of(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PageInfo<>();
    }
}
