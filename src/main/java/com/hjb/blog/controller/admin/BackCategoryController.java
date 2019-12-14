package com.hjb.blog.controller.admin;


import com.hjb.blog.entity.normal.ArticleCategoryRef;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.service.normal.ArticleCategoryRefService;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类
 * @author h1525
 */
@Controller
@RequestMapping("/admin/category")
public class BackCategoryController {

    @Resource
    private ArticleService articleService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleCategoryRefService articleCategoryRefService;
    
    /**
     * 后台分类列表显示
     *
     * @return
     */
    @GetMapping(value = "")
    public String categoryList(Model model) {
        List<Category> categoryList = categoryService.selectAll();
        // 更新文章/子类型数量
        updateArticleOrCategoryCount(categoryList);
        model.addAttribute("categoryList", categoryList);
        return "Admin/Category/index";
    }


    /**
     * 后台添加分类提交
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/insertSubmit",method = RequestMethod.POST)
    public String insertCategorySubmit(Category category)  {
        categoryService.insertSelective(category);
        return "redirect:/admin/category";
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id)  {
        Category category = categoryService.selectById(id);
        // 根判断
        if (category.getCategoryPid() == 0) {
            Category cate = new Category();
            cate.setCategoryPid(id);
            int categoryCount = categoryService.selectCount(cate);
            // 无子类型的根才可以删除
            if (categoryCount == 0) {
                categoryService.deleteCategory(id);
            }
        } else {
            // 禁止删除有文章的分类
            int count = articleService.countArticleByCategoryId(id);
            if (count == 0) {
                categoryService.deleteCategory(id);
            }
        }
        return "redirect:/admin/category";
    }

    /**
     * 编辑分类页面显示
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public ModelAndView editCategoryView(@PathVariable("id") Integer id)  {
        ModelAndView modelAndView = new ModelAndView();

        Category category =  categoryService.selectById(id);
        modelAndView.addObject("category",category);

        List<Category> categoryList = categoryService.selectAll();
        // 更新文章/子类型数量
        updateArticleOrCategoryCount(categoryList);
        modelAndView.addObject("categoryList",categoryList);

        modelAndView.setViewName("Admin/Category/edit");
        return modelAndView;
    }

    /**
     * 编辑分类提交
     *
     * @param category 分类
     * @return 重定向
     */
    @RequestMapping(value = "/editSubmit",method = RequestMethod.POST)
    public String editCategorySubmit(Category category)  {
        categoryService.update(category);
        return "redirect:/admin/category";
    }

    /**
     * 更新文章/子类型数量
     *
     * @param categoryList
     */
    private void updateArticleOrCategoryCount(List<Category> categoryList) {
        for (Category cate : categoryList) {
            if (cate.getCategoryPid() == 0) {
                Category t = new Category();
                t.setCategoryPid(cate.getId());
                int count = categoryService.selectCount(t);
                cate.setCategoryCount(count);
            } else {
                ArticleCategoryRef acr = new ArticleCategoryRef();
                acr.setCategoryId(cate.getId());
                int count = articleCategoryRefService.selectCount(acr);
                cate.setCategoryCount(count);
            }
        }
    }
}
