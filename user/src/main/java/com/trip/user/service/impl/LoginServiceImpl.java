package com.trip.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.common.constant.UserConstant;
import com.trip.common.exception.BusinessException;
import com.trip.user.vo.ArticleAuthor;
import com.trip.user.vo.UserResponseVo;
import com.trip.user.dao.UserDao;
import com.trip.user.pojo.UserPojo;
import com.trip.user.service.LoginService;
import com.trip.user.vo.RegisterVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl extends ServiceImpl<UserDao, UserPojo> implements LoginService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) throws BusinessException{
        UserPojo existUser = this.getOne(new QueryWrapper<UserPojo>().eq("username", registerVo.getUsername()));
        UserPojo newUser = new UserPojo();
        if (existUser == null){
            newUser.setUsername(registerVo.getUsername());
            newUser.setPassword(BCrypt.hashpw(registerVo.getPassword(), BCrypt.gensalt(UserConstant.BCRYPT_SALT)));
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
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
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
    public ArticleAuthor getArticleAuthor(Long id) {
        UserPojo user = this.getOne(new QueryWrapper<UserPojo>().eq("id", id));
        ArticleAuthor author = new ArticleAuthor();
        author.setId(id);
        author.setUsername(user.getUsername());
        return author;
    }
}
