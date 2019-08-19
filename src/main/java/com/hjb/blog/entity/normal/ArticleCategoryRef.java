package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "article_category_ref")
public class ArticleCategoryRef extends BaseEntity {
    /**
     * 文章id
     */
    @Column(name = "article_id")
    private Integer articleId;

    /**
     * 类型id
     */
    @Column(name = "category_id")
    private Integer categoryId;

    public ArticleCategoryRef(Integer id) {
        super(id);
    }

}