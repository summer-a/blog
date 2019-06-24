package com.hjb.blog.entity.dto;

import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Tag;

/**
 * 导航转换<br>
 * 提取Tag或Category转换成导航的信息
 *
 * @author 胡江斌
 * @version 1.0
 * @title: NavVO
 * @projectName blog
 * @description: TODO
 * @date 2019/6/23 22:20
 */
public class NavVO {

    /**
     * 标签或分类id
     */
    private Integer id;
    /**
     * 标签或分类url
     */
    private String url;
    /**
     * 标签或分类名
     */
    private String name;
    /**
     * tag url
     */
    private static final String TAG_URL = "/tag/";
    /**
     * category url
     */
    private static final String CATEGORY_URL = "/category/";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NavVO() {
    }

    public NavVO(Integer id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    /**
     * 转换tag
     *
     * @param tag 标签
     * @return
     */
    public static NavVO covert(Tag tag) {
        if (tag != null) {
            return new NavVO(tag.getId(), TAG_URL + tag.getId(), tag.getTagName());
        }
        return null;
    }

    /**
     * 转换category
     *
     * @param category 分类
     * @return
     */
    public static NavVO covert(Category category) {
        if (category != null) {
            return new NavVO(category.getId(), CATEGORY_URL + category.getId(), category.getCategoryName());
        }
        return null;
    }

    @Override
    public String toString() {
        return "NavVO{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
