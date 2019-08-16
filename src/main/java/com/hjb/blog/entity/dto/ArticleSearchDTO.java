package com.hjb.blog.entity.dto;

import com.hjb.blog.entity.base.EsBaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 博客文档<br>
 * indexName相当于数据库名<br>
 *     document相当于表名
 * @author 胡江斌
 * @version 1.0
 * @title: DocumentInfoDTO
 * @projectName blog
 * @description: TODO
 * @date 2019/8/8 12:03
 */
@Data
//@Document(indexName = "article", type = "document")
public class ArticleSearchDTO extends EsBaseEntity implements Serializable {

    /** 文章标题 */
    private String title;
    /** 文章摘要 */
    private String summary;
    /** 文章浏览数 */
    private Long viewCount;
    /** 文章赞数 */
    private Long likeCount;
    /** 文章评论数 */
    private Long commentCount;
    /** 发表时间 */
    private String date;

}
