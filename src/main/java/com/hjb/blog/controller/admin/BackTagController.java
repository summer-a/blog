package com.hjb.blog.controller.admin;


import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.field.SessionFields;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.TagService;
import com.xiaoleilu.hutool.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/tag")
public class BackTagController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    /**
     * 后台标签列表显示
     *
     * @return
     */
    @GetMapping(value = "")
    public ModelAndView index() {
        ModelAndView modelandview = new ModelAndView();
        List<Tag> tags = tagService.selectAll();
        List<Tag> newTags = new JSONArray(tags).toList(Tag.class);
        // 更新文章数
        for (Tag tag : newTags) {
            int count = articleService.countArticleByTagId(tag.getId());
            tag.setArticleCount(count);
        }
        modelandview.addObject(SessionFields.BLOG_TAG_LIST, newTags);

        modelandview.setViewName("Admin/Tag/index");
        return modelandview;
    }


    /**
     * 后台添加分类页面显示
     *
     * @param tag
     * @return
     */
    @PostMapping(value = "/insertSubmit")
    public String insertTagSubmit(Tag tag) {
        tagService.insertSelective(tag);
        return "redirect:/admin/tag";
    }

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return
     */
    @PostMapping(value = "/delete/{id}")
    public String deleteTag(@PathVariable("id") Integer id) {
        /*Integer count = articleService.countArticleByTagId(id);
        if (count == 0) {
            tagService.deleteTag(id);
        }*/
        return "redirect:/admin/tag";
    }

    /**
     * 编辑标签页面显示
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/edit/{id}")
    public ModelAndView editTagView(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        Tag tag = tagService.selectById(id);
        modelAndView.addObject(SessionFields.BLOG_TAG, tag);

        List<Tag> tagList = tagService.selectAll();
        modelAndView.addObject(SessionFields.BLOG_TAG_LIST, tagList);

        modelAndView.setViewName("Admin/Tag/edit");
        return modelAndView;
    }


    /**
     * 编辑标签提交
     *
     * @param tag
     * @return
     */
    @PostMapping(value = "/editSubmit")
    public String editTagSubmit(Tag tag) {
        tagService.update(tag);
        return "redirect:/admin/tag";
    }
}
