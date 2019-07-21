package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.*;

@Table(name = "article_tag_ref")
public class ArticleTagRef extends BaseEntity {
    /**
     * 文章id
     */
    @Column(name = "article_id")
    private Integer articleId;

    /**
     * 标签id
     */
    @Column(name = "tag_id")
    private Integer tagId;

    /**
     * 获取文章id
     *
     * @return article_id - 文章id
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * 设置文章id
     *
     * @param articleId 文章id
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取标签id
     *
     * @return tag_id - 标签id
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * 设置标签id
     *
     * @param tagId 标签id
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

}