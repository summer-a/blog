package com.hjb.blog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.entity.normal.User;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CategoryService;
import com.hjb.blog.service.normal.TagService;
import com.hjb.blog.util.AdminUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author liuyanzhao
 */
@Controller
@RequestMapping("/admin/article")
public class BackArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 后台文章列表显示
     *
     * @return modelAndView
     */
    @GetMapping(value = "")
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam(required = false) Integer status,
                        Model model) {

        Article articleParam = new Article();

        if (status == null) {
            // 设置分页时当前的url
            model.addAttribute("pageUrlPrefix", "/admin/article?pageIndex");
        } else {
            articleParam.setArticleStatus(status);
            model.addAttribute("pageUrlPrefix", "/admin/article?status=" + status + "&pageIndex");
        }

        PageInfo<Article> articlePageInfo = articleService.page(pageIndex, pageSize, articleParam, OrderField.NONE);
        model.addAttribute("pageInfo", articlePageInfo);
        return "Admin/Article/index";
    }


    /**
     * 后台添加文章页面显示
     *
     * @return
     */
    @GetMapping(value = "/insert")
    public String insertArticleView(Model model) {
        List<Category> categoryList = categoryService.selectAll();
        List<Tag> tagList = tagService.selectAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("tagList", tagList);
        return "Admin/Article/insert";
    }

    /**
     * 后台添加文章提交操作
     *
     * @param articleParam
     * @return
     */
    @PostMapping(value = "/insertSubmit")
    public String insertArticleSubmit(HttpSession session, Article articleParam) {
        Article article = new Article();
        //用户ID
        User currentUser = AdminUserUtils.getCurrentUser();

        if (currentUser != null) {
            article.setArticleUserId(currentUser.getId());
        }
        // 文章标题
        article.setArticleTitle(articleParam.getArticleTitle());
        // 文章摘要
        article.setArticleSummary(articleParam.getArticleSummary());
        // 内容
        article.setArticleContent(articleParam.getArticleContent());
        // 状态
        article.setArticleStatus(articleParam.getArticleStatus());
        // 填充分类
        List<Category> categoryList = new ArrayList<>();
        /*if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleParentCategoryId()));
        }
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleChildCategoryId()));
        }*/
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        /*if (articleParam.getArticleTagIds() != null) {
            for (int i = 0; i < articleParam.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(articleParam.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }*/
        article.setTagList(tagList);

        articleService.insertSelective(article);
        return "redirect:/admin/article";
    }


    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    @PostMapping(value = "/delete/{id}")
    public void deleteArticle(@PathVariable("id") Integer id) {
        articleService.deleteByPrimaryKey(id);
    }


    /**
     * 编辑文章页面显示
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String editArticleView(Model model, @PathVariable("id") Integer id) {


        Article article = articleService.selectOneForFullArticle(new Article(id));
        model.addAttribute("article", article);

        List<Tag> tagList = tagService.selectAll();
        List<Tag> tagSelecteds = article.getTagList();
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList = JSON.parseArray(JSONObject.toJSONString(tagList), Tag.class);
            for (Tag tag : tagList) {
                for (Tag tagSelect : tagSelecteds) {
                    if (Objects.equals(tagSelect.getId(), tag.getId())) {
                        // 为null则表示选中
                        tag.setStatus(null);
                        break;
                    }
                }
            }
        }
        model.addAttribute("tagList", tagList);

        List<Category> categoryList = categoryService.selectAll();
        model.addAttribute("categoryList", categoryList);

        return "Admin/Article/edit";
    }


    /**
     * 编辑文章提交
     *
     * @param articleParam
     * @return
     */
    @PostMapping(value = "/editSubmit")
    public String editArticleSubmit(Article articleParam,
                                    @RequestParam(value = "childId", required = true) Integer childId,
                                    @RequestParam(value = "articleTagIds", required = false) List<Integer> articleTagIds) {
        // 填充分类
        List<Category> categoryList = new ArrayList<>();
        if (childId != null) {
            categoryList.add(new Category(childId));
        }

        articleParam.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(articleTagIds)) {
            articleTagIds.stream().forEach(r -> tagList.add(new Tag(r)));
            articleParam.setTagList(tagList);
        }
        articleService.update(articleParam);
        return "redirect:/admin/article";
    }

    /**
     * 获取分类
     * @return
     */
    @ResponseBody
    @GetMapping("/getCategoryList")
    public List<Category> getCategoryList() {
        List<Category> categoryList = categoryService.selectAll();
        return categoryList;
    }
}



