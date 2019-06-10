package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.*;

@Table(name = "article_category_ref")
public class ArticleCategoryRef extends BaseEntity {
    /**
     * 文章id
     */
    @Id
    @Column(name = "article_id")
    private Integer articleId;

    /**
     * 类型id
     */
    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 获取类型id
     *
     * @return category_id - 类型id
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置类型id
     *
     * @param categoryId 类型id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

}