package com.trip.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trip.common.exception.BusinessException;
import com.trip.user.pojo.UserPojo;
import com.trip.user.vo.ArticleAuthor;
import com.trip.user.vo.RegisterVo;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public interface LoginService extends IService<UserPojo> {
    void register(RegisterVo registerVo) throws BusinessException;

    Map<String, Object> login(RegisterVo registerVo) throws BusinessException, JsonProcessingException, UnsupportedEncodingException;

    ArticleAuthor getArticleAuthor(Long id);
}
