package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
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