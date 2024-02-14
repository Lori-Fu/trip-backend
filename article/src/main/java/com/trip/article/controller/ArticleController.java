package com.trip.article.controller;

import com.trip.article.pojo.ArticleRankPojo;
import com.trip.article.service.ArticleContentService;
import com.trip.article.service.ArticleRankService;
import com.trip.article.vo.ArticleBriefVoToEs;
import com.trip.article.vo.ArticleContentResponseVo;
import com.trip.article.vo.ArticleVo;
import com.trip.article.annotation.RequireLogin;
import com.trip.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ArticleController {

    @Autowired
    ArticleContentService articleContentService;

    @Autowired
    ArticleRankService articleRankService;

    @PostMapping("/new")
    @RequireLogin
    public R createArticle(@RequestBody ArticleVo articleVo){
        try{
            Long article_id = articleContentService.createArticle(articleVo);
            return R.ok().put("data", article_id);
        }catch (Exception e){
            return R.error(500, "Internal Server Error");
        }
    }

    @GetMapping("/{id}")
    public R getArticle(@PathVariable("id") Long id){
        try{
            Map<String, Object> article = articleContentService.getArticle(id);
            articleContentService.incrementViewNum(id);
            return R.ok().put("data", article);
        }catch (Exception e){
            return R.error(500, "Internal Server Error");
        }
    }

    @GetMapping("/article/author/{user_id}")
    R getArticleByAuthor(@RequestParam Long user_id){
        List<ArticleContentResponseVo> articles = articleContentService.getArticleByUser(user_id);
        return R.ok().put("data",articles);
    };

    @RequireLogin
    @GetMapping("/myArticle")
    R getMyArticle(){
        List<ArticleContentResponseVo> articles = articleContentService.getMyArticle();
        return R.ok().put("data",articles);
    };


    @GetMapping("/hot")
    R getHotArticle(){
        List<ArticleRankPojo> articles = articleRankService.list();
        return R.ok().put("data",articles);
    };

    @GetMapping("/initArticleES")
    R initArticleES(Integer current, Integer limit){
        List<ArticleBriefVoToEs> list = articleContentService.initES(current, limit);
        return R.ok().put("data",list);
    };

    @GetMapping("/myCollections")
    R getArticles(@RequestParam List<Long> list){
        List<ArticleBriefVoToEs> articles = articleContentService.getArticles(list);
        return R.ok().put("data",articles);
    }

}
