package com.trip.user.controller;

import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.user.annotation.RequireLogin;
import com.trip.user.service.LoginService;
import com.trip.user.vo.ArticleAuthor;
import com.trip.user.vo.RegisterVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

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
            loginService.register(registerVo);
        }catch (BusinessException e){
            return R.error(e.getCode(), e.getMessage());
        }
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody RegisterVo registerVo){
        try{
            Map<String, Object> user = loginService.login(registerVo);
            return R.ok().put("userInfo", user);
        }catch (BusinessException e) {
            return R.error(e.getCode(), e.getMessage());
        }catch (Exception e){
            return R.error(500, "Internal Server Error");
        }
    }

    @GetMapping("/articleAuthor")
    R getArticleAuthor(@RequestParam Long id){
        ArticleAuthor author = loginService.getArticleAuthor(id);
        return R.ok().put("data", author);
    };
}
