package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
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

    public ArticleTagRef(Integer id) {
        super(id);
    }

}