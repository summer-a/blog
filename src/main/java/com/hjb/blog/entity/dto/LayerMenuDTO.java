package com.hjb.blog.entity.dto;

import com.hjb.blog.entity.normal.Category;

import java.io.Serializable;
import java.util.List;

/**
 * 层叠菜单
 * @author 胡江斌
 * @version 1.0
 * @title: LayerMenuDTO
 * @projectName blog
 * @description: TODO
 * @date 2019/6/14 0:28
 */
public class LayerMenuDTO<T> implements Serializable {

    private Category parentCategory;

    private List<T> childCategory;

    public LayerMenuDTO(Category parentCategory, List<T> childCategory) {
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
    }

    public LayerMenuDTO(){}

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<T> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<T> childCategory) {
        this.childCategory = childCategory;
    }

    @Override
    public String toString() {
        return "LayerMenuDTO{" +
                "parentCategory=" + parentCategory +
                ", childCategory=" + childCategory +
                '}';
    }
}
