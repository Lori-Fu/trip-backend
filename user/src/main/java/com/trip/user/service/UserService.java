package com.trip.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trip.common.exception.BusinessException;
import com.trip.user.pojo.UserPojo;
import com.trip.user.vo.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public interface UserService extends IService<UserPojo> {
    void register(RegisterVo registerVo) throws BusinessException;

    Map<String, Object> login(RegisterVo registerVo) throws BusinessException, JsonProcessingException, UnsupportedEncodingException;

    UserInfoVo getUserInfo(Long id);

    Map<String,List<Integer>> getStatList(Long id);

    void collectArticle(Long article_id);

    void incrementCollectNum(Long article_id, Integer delta);

    void likeArticle(Long article_id);

    void incrementThumbupNum(Long article_id, Integer delta);

    List<ArticleBriefVoToEs> myCollections();

    List<ArticleBriefVoToEs> myLikes();

}
