package com.hjb.blog.util;

import com.alibaba.fastjson.JSONArray;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.normal.Category;
import com.hjb.blog.entity.normal.Tag;
import com.hjb.blog.entity.vo.ResponseVO;
import com.hjb.blog.service.normal.ArticleService;
import com.hjb.blog.service.normal.TagService;
import okhttp3.Request;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.selector.Html;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 转载工具
 * @author 胡江斌
 * @version 1.0
 * @title: ReprintUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/8/31 14:02
 */
public class ReprintUtils {

    private static final String CSDN_HOME = "https://www.csdn.net";

    private static ArticleService articleService = SpringUtils.getBean(ArticleService.class);

    private static TagService tagService = SpringUtils.getBean(TagService.class);


    /**
     * 获取csdn文章列表
     * @param type 文章类型
     * @return
     */
    public static List<Article> csdnList(String type) {

        // 结果
        List<Article> list = new ArrayList<>();

        // 分类链接补充
        String typeUrl = "";
        if (!StringUtils.isEmpty(type)) {
            typeUrl = "/nav/" + type;
        }

        ResponseVO responseVO = HttpUtils.get(CSDN_HOME + typeUrl, new Request.Builder());
        Html html = responseVO.getHtml();
        List<String> liList = html.xpath("//ul[@id='feedlist_id']/li[@class='clearfix']").all();
        if (!CollectionUtils.isEmpty(liList)) {
            for (String liStr : liList) {
                Html li = new Html(liStr);

                Article article = new Article();

                String id = li.xpath("//li/@data-id").get();
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                // id
                article.setId(Integer.parseInt(id));
                // 标题
                String title = li.xpath("//div[@class='title']/h2/a/text()").get();
                article.setArticleTitle(StringUtils.isEmpty(title) ? "" : title.trim());
                // url
                String url = li.xpath("//div[@class='title']/h2/a/@href").get();
                article.setArticleFromUrl(url);
                // 摘要
                String summary = li.xpath("//div[@class='summary']/text()").get();
                // 只取前130位
                summary = (!StringUtils.isEmpty(summary) && summary.length() > 130) ? summary.substring(0,  130) : summary;
                article.setArticleSummary(StringUtils.isEmpty(summary) ? "" : summary.trim());
                // 作者名
                String author = li.xpath("//dd[@class='name']/a/text()").get();
                article.setArticleFromAuthor(StringUtils.isEmpty(author) ? "" : author.trim());
                // 作者url
                String authorUrl = li.xpath("//dd[@class='name']/a/@href").get();
                article.setArticleFromAuthorUrl(authorUrl);
                // 作者头像
                /*String authorAvatar = li.xpath("//a[@class='user_img']/img/@src").get();
                article.setAuthorAvatar(authorAvatar);*/
                // 其他信息
                article.setArticleFrom("csdn");

                // 获取分类
                /*
                 * 12	11	Java
                 * 13	11	前端
                 * 14	11	架构
                 * 15	11	数据库
                 * 16	11	云计算/大数据
                 * 17	11	计算机基础
                 * 18	11	其他
                 */
                List<Category> categories = new ArrayList<>();
                Category category = null;
                type = type == null ? "" : type.trim();
                switch(type) {
                    case "java":
                        category = new Category(12);
                        break;
                    case "web":
                        category = new Category(13);
                        break;
                    case "arch":
                        category = new Category(14);
                        break;
                    case "db":
                        category = new Category(15);
                        break;
                    case "cloud":
                        category = new Category(16);
                        break;
                    case "fund":
                        category = new Category(17);
                        break;
                    default:
                        category = new Category(18);
                        break;
                }

                categories.add(category);
                article.setCategoryList(categories);
                article.setCreateTime(LocalDateTime.now());
                article.setUpdateTime(LocalDateTime.now());

                list.add(article);
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 转载csdn
     * @param articles
     * @return
     */
    public static void csdnReprint(List<Article> articles) {

        for (Article article : articles) {

            article.setArticleStatus(1);
            article.setArticleUserId(1);
            article.setArticleOrder(5);
            String url = article.getArticleFromUrl();
            if (StringUtils.isEmpty(url)) {
                continue;
            }
            // 获取文章详情
            ResponseVO responseVO = HttpUtils.get(url, new Request.Builder());
            Html html = responseVO.getHtml();

            // 获取内容
            String articleContent = html.xpath("//div[@id='article_content']").get();
            article.setArticleContent(articleContent);

            // 获取标签
            List<String> tags = html.xpath("//span[@class='artic-tag-box']/a/text()").all();
            if (!CollectionUtils.isEmpty(tags)) {
                List<Tag> all = tagService.selectAll();
                if (!CollectionUtils.isEmpty(all)) {
                    // redis缓存原因,直接使用会报不能由LinkedHashMap转对象的错
                    // 转换
                    all = JSONArray.parseArray(JSONArray.toJSONString(all), Tag.class);
                    Iterator<Tag> it = all.iterator();
                    while (it.hasNext()) {
                        Tag next = it.next();
                        boolean flag = false;
                        for (String tag : tags) {
                            if (Objects.equals(tag, next.getTagName())) {
                                flag = true;
                                break;
                            }
                        }
                        // 没找到则删除
                        if (!flag) {
                            it.remove();
                        }
                    }
                    article.setTagList(all);
                }
            }
            articleService.insertSelective(article);
        }
    }

}
