package com.hjb.blog;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.dto.ArticleSearchDTO;
import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.service.common.ElasticsearchService;
import com.hjb.blog.service.normal.ArticleService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
@Transactional
@Rollback
public class BlogApplicationTests {


    @Resource
    private ArticleService articleService;

    @Resource
    private RestHighLevelClient client;

    @Resource
    private ElasticsearchService<ArticleSearchDTO> elasticsearchService;

    @Test
    public void search() {
        String keyword = "Java";
        PageInfo<Map<String, Object>> search1 =
                elasticsearchService.search(0, 10, "article", "document", "title", "id,title", null, keyword);
        System.out.println("s1=" + JSON.toJSONString(search1));

        PageInfo<Map<String, Object>> search2 = elasticsearchService.search(0, 10, "article", "document", "title,content,date", "id,title,content,summary,viewCount,likeCount,commentCount,date", null, keyword);
        System.out.println("s2=" + JSON.toJSONString(search2));
    }

    @Test
    public void contextLoads() {

        List<Article> articleList = articleService.selectListForFullArticle(new Article());

        articleList.forEach(r -> {
            ArticleSearchDTO info = new ArticleSearchDTO();
            info.setId(r.getId().longValue());
            info.setTitle(r.getArticleTitle());
            info.setContent(r.getArticleContent());
            info.setCommentCount(r.getArticleCommentCount().longValue());
            info.setLikeCount(r.getArticleLikeCount().longValue());
            info.setViewCount(r.getArticleViewCount().longValue());
            info.setSummary(r.getArticleSummary());
            info.setDate(r.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd号")));
        });
        System.out.println("ok");

        GetRequest getRequest = new GetRequest();
        try {
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(documentFields);
        } catch (IOException e) {e.printStackTrace();

        }
    }

}
