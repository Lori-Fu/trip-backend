package com.trip.article.controller;

import com.trip.article.service.ArticleContentService;
import com.trip.article.service.ArticleDayService;
import com.trip.article.service.ArticleRouteService;
import com.trip.article.vo.ArticleResponseVo;
import com.trip.article.vo.ArticleVo;
import com.trip.article.annotation.RequireLogin;
import com.trip.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ArticleController {

    @Autowired
    ArticleContentService articleContentService;

    @Autowired
    ArticleDayService articleDayService;

    @Autowired
    ArticleRouteService articleRouteService;


    @GetMapping("/test")
    public R test(){
        return R.ok();
    }

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
            ArticleResponseVo article = articleContentService.getArticle(id);
            return R.ok().put("data", article);
        }catch (Exception e){
            return R.error(500, "Internal Server Error");
        }
    }

}
