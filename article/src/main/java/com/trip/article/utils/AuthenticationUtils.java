package com.trip.article.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.article.interceptor.LoginInterceptor;
import com.trip.article.vo.UserResponseVo;
import com.trip.common.constant.UserConstant;
import com.trip.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


abstract public class AuthenticationUtils {

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null){
            throw new BusinessException(500, "Server Error");
        }
        return attr.getRequest();
    }


    public static String getToken(){
        return getRequest().getHeader("Token");
    }

    public static UserResponseVo getUser() throws JsonProcessingException {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        if (userResponseVo != null){
            return userResponseVo;
        }
        String token = getToken();
        if (StringUtils.hasText(token)) {
            JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(UserConstant.JWT_SECRET.getBytes())).build();
            Claims jwt = parser.parseSignedClaims(token).getPayload();
            String uuid = (String) jwt.get("uuid");
            StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
            String s = redisTemplate.opsForValue().get(uuid);
            if (s == null) {
                return null;
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                UserResponseVo user = objectMapper.readValue(s, UserResponseVo.class);
                return user;
            }
        }
        return null;
    }
}