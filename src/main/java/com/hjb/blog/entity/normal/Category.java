package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    /**
     * 父id
     */
    @Column(name = "category_pid")
    private Integer categoryPid;

    /**
     * 类型名
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 类型描述
     */
    @Column(name = "category_description")
    private String categoryDescription;

    /**
     * 类型排序
     */
    @Column(name = "category_order")
    private Integer categoryOrder;

    /**
     * 类型图标
     */
    @Column(name = "category_icon")
    private String categoryIcon;

    /**
     * 分类下文章数/其他类目数
     */
    private int categoryCount;

    public Category(Integer id) {
        super(id);
    }

    public Category(Integer id, String categoryName) {
        super(id);
        this.categoryName = categoryName;
    }

    public static Category getDefault() {
        return new Category(-1, "未分类");
    }

}