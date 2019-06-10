package com.hjb.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
@Transactional
@Rollback
public class BlogApplicationTests {


    @Test
    public void contextLoads() {
       /* int start = 1;

        int sum = 10;
        PageHelper.startPage(start, sum);
        List<Article> articles = articleMapper.selectAll();
        PageInfo<Article> articlePageInfo = new PageInfo<>(articles);
        articles = articlePageInfo.getList();
        articles.forEach(r->{
            System.out.println(r.getArticleTitle());
        });*/
    }

}
