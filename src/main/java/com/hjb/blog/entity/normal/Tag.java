package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "tag")
public class Tag extends BaseEntity {

    /**
     * 标签名
     */
    @Column(name = "tag_name")
    private String tagName;

    /**
     * 标签描述
     */
    @Column(name = "tag_description")
    private String tagDescription;

    @Column(name = "status")
    private Boolean status;

    /**
     * 非数据库字段,指定属于该标签的文章的数量
     */
    private int articleCount;

    public Tag(Integer id) {
        super(id);
    }
}