package com.trip.user.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.common.constant.UserConstant;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.user.feign.ArticleFeignService;
import com.trip.user.interceptor.LoginInterceptor;
import com.trip.user.vo.*;
import com.trip.user.dao.UserDao;
import com.trip.user.pojo.UserPojo;
import com.trip.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.trip.common.constant.ArticleConstant.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPojo> implements UserService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ArticleFeignService articleFeignService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) throws BusinessException{
        UserPojo existUser = this.getOne(new QueryWrapper<UserPojo>().eq("username", registerVo.getUsername()));
        UserPojo newUser = new UserPojo();
        if (existUser == null){
            newUser.setUsername(registerVo.getUsername());
            newUser.setPassword(BCrypt.hashpw(registerVo.getPassword(), BCrypt.gensalt(UserConstant.BCRYPT_SALT)));
            newUser.setCollect_list(new ArrayList<>());
            newUser.setThumbup_list(new ArrayList<>());
            super.save(newUser);
        }else{
            throw new BusinessException(400, "Username Already Exist");
        }
    }

    @Override
    public Map<String, Object> login(RegisterVo registerVo) throws BusinessException, JsonProcessingException {
        UserPojo existUser = this.getOne(new QueryWrapper<UserPojo>().eq("username", registerVo.getUsername()));
        if (existUser == null || !BCrypt.checkpw(registerVo.getPassword(), existUser.getPassword())){
//            if (existUser == null || !registerVo.getPassword().equals(existUser.getPassword())){
            throw new BusinessException(401, "Invalid Username and Password");
        }

        UserResponseVo userResponseVo = new UserResponseVo();
        userResponseVo.setId(existUser.getId());
        userResponseVo.setUsername(existUser.getUsername());
        Long now = System.currentTimeMillis();
        userResponseVo.setLogin_time(now);
        userResponseVo.setExpire_time(now + UserConstant.EXPIRE_TIME * 60 * 1000);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String userAsString = objectMapper.writeValueAsString(userResponseVo);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(uuid, userAsString, UserConstant.EXPIRE_TIME, TimeUnit.MINUTES);

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid",uuid);
        String token = Jwts.builder().setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(UserConstant.JWT_SECRET.getBytes())).compact();
        claims.clear();
        claims.put("token", token);
        claims.put("user", userResponseVo);
        return claims;
    }

    @Override
    public UserInfoVo getUserInfo(Long id) {
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", id));
        UserInfoVo author = new UserInfoVo();
        author.setId(id);
        author.setUsername(user.getUsername());
        return author;
    }

    @Override
    public Map<String, List<Integer>> getStatList(Long id) {
        Map<String, List<Integer>> map = new HashMap<>();
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", id));
        map.put("collect_list", user.getCollect_list());
        map.put("thumbup_list", user.getThumbup_list());
        return map;
    }

    @Override
    public void collectArticle(Long article_id) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();

        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", userResponseVo.getId()));
        if (user.getCollect_list().contains(Integer.valueOf(article_id+""))){
            user.getCollect_list().remove(Integer.valueOf(article_id+""));
            incrementCollectNum(article_id, -1);
        }else{
            user.getCollect_list().add(Integer.valueOf(article_id+""));
            incrementCollectNum(article_id, 1);

        }
        this.saveOrUpdate(user);
    }

    public void incrementCollectNum(Long article_id, Integer delta) {
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_COLLECT_NUM);
        ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_COLLECT_NUM, (Integer)o + delta);
    }

    public void incrementThumbupNum(Long article_id, Integer delta) {
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_LIKE_NUM);
        ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_LIKE_NUM, (Integer)o + delta);
    }

    @Override
    public void likeArticle(Long article_id) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", userResponseVo.getId()));
        if (user.getThumbup_list().contains(Integer.valueOf(article_id+""))){
            user.getThumbup_list().remove(Integer.valueOf(article_id+""));
            incrementThumbupNum(article_id, -1);
        }else{
            user.getThumbup_list().add(Integer.valueOf(article_id+""));
            incrementThumbupNum(article_id, 1);
        }
        this.saveOrUpdate(user);
    }

    @Override
    public List<ArticleBriefVoToEs> myCollections() {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", userResponseVo.getId()));
        List<Integer> list = user.getCollect_list();
        if (list.size() == 0){
            return new ArrayList<>();
        }
        R result= articleFeignService.getArticles(list.stream().map(e -> Long.valueOf(e+"")).collect(Collectors.toList()));
        if (result.getCode() != 0){
            throw new BusinessException(500, "Feign Service Error");
        }
        List<ArticleBriefVoToEs> articles = result.getData(new TypeReference<>() {});
        return articles;
    }

    @Override
    public List<ArticleBriefVoToEs> myLikes() {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", userResponseVo.getId()));
        List<Integer> list = user.getThumbup_list();
        if (list.size() == 0){
            return new ArrayList<>();
        }
        R result= articleFeignService.getArticles(list.stream().map(e -> Long.valueOf(e+"")).collect(Collectors.toList()));
        if (result.getCode() != 0){
            throw new BusinessException(500, "Feign Service Error");
        }
        List<ArticleBriefVoToEs> articles = result.getData(new TypeReference<>() {});
        return articles;
    }
}
