package com.hjb.blog.service.common;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.base.EsBaseEntity;
import com.hjb.blog.entity.dto.ArticleSearchDTO;
import com.hjb.blog.entity.vo.ResultVO;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 搜索服务
 *
 * @author 胡江斌
 * @version 1.0
 * @title: ElasticsearchService
 * @projectName blog
 * @description: TODO
 * @date 2019/8/10 8:17
 */
public interface ElasticsearchService<T> {

    /**
     * 创建索引
     *
     * @param index 索引名
     * @return
     */
    ResultVO createIndex(String index) throws IOException;

    /**
     * 添加记录
     *
     * @param index 索引名
     * @param type  类型
     * @param t     存入对数
     * @return
     */
    ResultVO add(String index, String type, T t) throws IOException;

    /**
     * 删除记录
     *
     * @param index 索引名
     * @param type  类型
     * @param id    id
     * @return
     */
    ResultVO delete(String index, String type, Long id) throws IOException;

    /**
     * 更新记录
     *
     * @param index 索引名
     * @param type  类型
     * @param t     对象
     * @return
     */
    ResultVO update(String index, String type, T t) throws IOException;

    /**
     * 获取单个记录
     *
     * @param index 索引名
     * @param type  类型
     * @param id    id
     * @return
     */
    Map<String, Object> getOne(String index, String type, Long id) throws IOException;

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
    PageInfo<Map<String, Object>> search(Integer pageNum, Integer pageSize, String index, String type, String fields, String includeFields, String excludeFields, String keyword);

}
