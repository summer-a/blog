package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: Menu
 * @projectName blog
 * @description: TODO
 * @date 2019/6/16 2:44
 */
@Data
public class Menu extends BaseEntity {

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_icon")
    private String menuIcon;

    @Column(name = "menu_order")
    private String menuOrder;

    @Column(name = "menu_status")
    private Boolean menuStatus;

    @Column(name = "menu_permission")
    private Boolean menuPermission;

}
