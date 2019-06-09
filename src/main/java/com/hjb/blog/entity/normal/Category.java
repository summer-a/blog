package com.hjb.blog.entity.normal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Category {
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

    /**
     * 最后更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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

    /**
     * 获取最后更新时间
     *
     * @return update_time - 最后更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param updateTime 最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}