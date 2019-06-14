package com.hjb.blog.service.normal.impl;

import com.hjb.blog.entity.dto.LayerMenuDTO;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.mapper.CategoryMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单分类Mapper
 *
 * @author 胡江斌
 * @version 1.0
 * @title: CategoryServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 22:38
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 查询分类，将其装换为分层式
     *
     * @param category 条件
     * @return
     */
    @Override
    public List<LayerMenuDTO<Category>> selectCategoryToLayerMenu(Category category) {
        List<Category> categories = categoryMapper.selectAll();

        List<LayerMenuDTO<Category>> layerMenuDTOS = categorysToLayerMenu(categories, 0);
        return layerMenuDTOS;
    }

    private List<LayerMenuDTO<Category>> categorysToLayerMenu(List<Category> categories, int parentId) {
        List<LayerMenuDTO<Category>> layerMenus = new ArrayList<>();

        if (!CollectionUtils.isEmpty(categories)) {
            Iterator<Category> iterator = categories.iterator();
            while (iterator.hasNext()) {
                Category category = iterator.next();
                LayerMenuDTO layerMenu = new LayerMenuDTO();
                if (category.getCategoryPid() == parentId) {
                    layerMenu.setParentCategory(category);
                    layerMenus.add(layerMenu);

                    iterator.remove();
                }
            }

            for (LayerMenuDTO menu : layerMenus) {

                List<Category> list = new ArrayList<>();

                Iterator<Category> it = categories.iterator();
                while (it.hasNext()) {
                    Category nextCategory = it.next();
                    if (nextCategory.getCategoryPid().equals(menu.getParentCategory().getId())) {

                        list.add(nextCategory);
                        menu.setChildCategory(list);

                        it.remove();
                    }
                }

            }
        }

        return layerMenus;
    }

}
