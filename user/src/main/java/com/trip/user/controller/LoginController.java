package com.trip.user.controller;

import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.user.annotation.RequireLogin;
import com.trip.user.service.UserService;
import com.trip.user.vo.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public R register(@Valid @RequestBody RegisterVo registerVo, BindingResult result){
        if (result.hasErrors()){
//            Map<String, String> error = result.getFieldErrors().stream().collect(Collectors.toMap(fieldError -> {
//                return fieldError.getField();
//            }, fieldError -> {
//                return fieldError.getDefaultMessage();
//            }));
            Map<String, String> error = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (k1, k2) -> k1));
            return R.error(400,"BAD_INPUT").put("error", error);
        }
        try{
            userService.register(registerVo);
        }catch (BusinessException e){
            return R.error(e.getCode(), e.getMessage());
        }
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody RegisterVo registerVo){
        try{
            Map<String, Object> user = userService.login(registerVo);
            return R.ok().put("userInfo", user);
        }catch (BusinessException e) {
            return R.error(e.getCode(), e.getMessage());
        }catch (Exception e){
            return R.error(500, "Internal Server Error");
        }
    }

    @GetMapping("/info/{id}")
    public R getUserInfo(@PathVariable Long id){
        UserInfoVo author = userService.getUserInfo(id);
        return R.ok().put("data", author);
    }


    @GetMapping("/statistic/{id}")
    public R getStatList(@PathVariable("id") Long id){
        Map<String,List<Integer>> statList = userService.getStatList(id);
        return R.ok().put("data", statList);
    }

    @RequireLogin
    @GetMapping("/collectArticle/{id}")
    public R collectArticle(@PathVariable("id") Long id){
        userService.collectArticle(id);
        return R.ok();
    }

    @RequireLogin
    @GetMapping("/likeArticle/{id}")
    public R likeArticle(@PathVariable("id") Long id){
        userService.likeArticle(id);
        return R.ok();
    }

    @RequireLogin
    @GetMapping("/myCollections")
    public R myCollections(){
        List<ArticleBriefVoToEs> articles = userService.myCollections();
        return R.ok().put("data", articles);
    }

    @RequireLogin
    @GetMapping("/myLikes")
    public R myLikes(){
        List<ArticleBriefVoToEs> articles = userService.myLikes();
        return R.ok().put("data", articles);
    }


}
