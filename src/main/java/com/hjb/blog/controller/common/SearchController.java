package com.hjb.blog.controller.common;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.base.EsBaseEntity;
import com.hjb.blog.entity.dto.ArticleSearchDTO;
import com.hjb.blog.entity.enums.ArticleStatus;
import com.hjb.blog.entity.enums.NoticeStatus;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Comment;
import com.hjb.blog.entity.normal.Notice;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.service.common.ElasticsearchService;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.CommentService;
import com.hjb.blog.service.normal.NoticeService;
import com.hjb.blog.service.normal.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索控制器
 * @author 胡江斌
 * @version 1.0
 * @title: SearchController
 * @projectName blog
 * @description: TODO
 * @date 2019/8/12 15:08
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Resource
    private ElasticsearchService<ArticleSearchDTO> elasticsearchService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    /**
     * 搜索
     * @param model
     * @param pageNum 当前页(es是从0页开始)
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("")
    public String getList(Model model,
                          @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(name = "s", required = true) String keyword) {

        // 开始搜索
        // PageInfo<Map<String, Object>> list = elasticsearchService.search(pageNum, pageSize, "article", "document", "title,content", "id,title,summary,viewCount,likeCount,commentCount,date", null, keyword);
        PageInfo<Article> articleList = articleService.selectLikeArticlesByTitle(keyword, pageNum, pageSize);
        model.addAttribute("articles", articleList);

        // 获取通知
        Notice notice = new Notice();
        notice.setNoticeStatus(NoticeStatus.NORMAL.getValue());
        List<Notice> notices = noticeService.select(notice);
        model.addAttribute("notices", notices);

        // 获取热评文章前10
        Article t = new Article();
        t.setArticleStatus(ArticleStatus.PUBLISH.getValue());
        PageInfo<Article> hotComments = articleService.page(1, 10, t, OrderField.orderByDesc("articleCommentCount"));
        model.addAttribute("hotCommentArticles", hotComments.getList());

        // 获取网站统计数据
        Map<String, String> siteBasicStatistics = new HashMap<>(5);
        siteBasicStatistics.put("commentCount", commentService.selectCount(new Comment()) + "");
        model.addAttribute("siteBasicStatistics", siteBasicStatistics);

        //标签列表显示
        List<Tag> allTagList = tagService.selectAll();
        model.addAttribute("tags", allTagList);

        // 返回搜索关键字
        model.addAttribute("keyword", keyword);
        return "Home/Page/searchList";
    }

    @GetMapping("/pullDownList")
    @ResponseBody
    public List<EsBaseEntity> searchTitle(String s) {
        // 存储结果集
        List<EsBaseEntity> result = new ArrayList<>(10);

        PageInfo<Map<String, Object>> searchResult = elasticsearchService.search(0, 10, "article", "document", "title", "id,title", null, s);
        List<Map<String, Object>> list = searchResult.getList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(res -> result.add(new EsBaseEntity(Long.parseLong(res.get("id").toString()), res.get("title").toString())));
        }
        return result;
    }
}
