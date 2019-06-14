package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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


    public Category() {}

    public Category(Integer id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static Category getDefault() {
        return new Category(-1, "未分类");
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

    /**
     * 获取父id
     *
     * @return category_pid - 父id
     */
    public Integer getCategoryPid() {
        return categoryPid;
    }

    /**
     * 设置父id
     *
     * @param categoryPid 父id
     */
    public void setCategoryPid(Integer categoryPid) {
        this.categoryPid = categoryPid;
    }

    /**
     * 获取类型名
     *
     * @return category_name - 类型名
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置类型名
     *
     * @param categoryName 类型名
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取类型描述
     *
     * @return category_description - 类型描述
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * 设置类型描述
     *
     * @param categoryDescription 类型描述
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * 获取类型排序
     *
     * @return category_order - 类型排序
     */
    public Integer getCategoryOrder() {
        return categoryOrder;
    }

    /**
     * 设置类型排序
     *
     * @param categoryOrder 类型排序
     */
    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    /**
     * 获取类型图标
     *
     * @return category_icon - 类型图标
     */
    public String getCategoryIcon() {
        return categoryIcon;
    }

    /**
     * 设置类型图标
     *
     * @param categoryIcon 类型图标
     */
    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

}