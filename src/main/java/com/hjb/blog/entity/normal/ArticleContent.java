package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "article_content")
public class ArticleContent extends BaseEntity {

    /**
     * 文章id
     */
    @Column(name = "article_id")
    private Integer articleId;

    /**
     * 文章内容
     */
    @Column(name = "article_content")
    private String articleContent;

    public ArticleContent(Integer id) {
        super(id);
    }
}