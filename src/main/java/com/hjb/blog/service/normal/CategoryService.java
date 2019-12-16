package com.hjb.blog.service.normal;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.service.base.BaseService;

import java.util.List;

/**
 * 菜单分类服务
 * @author 胡江斌
 * @version 1.0
 * @title: UserService
 * @projectName blog
 * @description: TODO
 * @date 2019/6/9 10:07
 */
public interface CategoryService extends BaseService<Category> {

    /**
     * 查询分类，将其装换为分层式
     * @param category 条件
     * @return
     */
    List<LayerMenuDTO<Category>> selectCategoryToLayerMenu(Category category);

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    int deleteCategory(int id);
}
