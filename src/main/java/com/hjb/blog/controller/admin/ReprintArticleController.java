package com.hjb.blog.controller.admin;

import com.hjb.blog.entity.normal.Article;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.util.ReprintUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 转载控制器
 * @author 胡江斌
 * @version 1.0
 * @title: SpiderArticleController
 * @projectName blog
 * @description: TODO
 * @date 2019/8/31 13:38
 */
@Controller
public class ReprintArticleController {

    @GetMapping("/csdn/list")
    @ResponseBody
    public List<Article> csdnList(@RequestParam(required = false) String type) {
        // 获取文章, 默认是'推荐'分类
        List<Article> articleVOS = ReprintUtils.csdnList(type);
        return articleVOS;
    }

    @GetMapping("/csdn/reprint")
    @ResponseBody
    public ResultVO csdnReprint(List<Article> reprintArticleVOS) {
        ReprintUtils.csdnReprint(reprintArticleVOS);
        return ResultVO.ok();
    }

    @GetMapping("/csdn/all")
    @ResponseBody
    public ResultVO csdnAll(@RequestParam(required = false) String type) {
        List<Article> articleVOS = ReprintUtils.csdnList(type);
        ReprintUtils.csdnReprint(articleVOS);
        return ResultVO.ok();
    }
}
