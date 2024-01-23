package com.trip.user.controller;

import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.user.service.LoginService;
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
        System.out.println(registerVo);
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
        System.out.println(registerVo);
        try{
            loginService.login(registerVo);
        }catch (BusinessException e){
            return R.error(e.getCode(), e.getMessage());
        }
        return R.ok();
    }

    @GetMapping("/login")
    public R login(){
        return R.ok();
    }


}
