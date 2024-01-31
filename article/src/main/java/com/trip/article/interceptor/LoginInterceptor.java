package com.trip.article.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.article.annotation.RequireLogin;
import com.trip.common.constant.UserConstant;
import com.trip.common.exception.BusinessException;
import com.trip.article.vo.UserResponseVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate redisTemplate;

    public static ThreadLocal<UserResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BusinessException {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        Class<?> controllerClass = hm.getBeanType();
        RequireLogin classAnnotation = controllerClass.getAnnotation(RequireLogin.class);
        RequireLogin methodAnnotation = hm.getMethodAnnotation(RequireLogin.class);
        if (classAnnotation == null && methodAnnotation == null){
            return true;
        }
        String token = request.getHeader("Token");
        try {
            JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(UserConstant.JWT_SECRET.getBytes())).build();
            Claims jwt = parser.parseSignedClaims(token).getPayload();
            String uuid = (String) jwt.get("uuid");
            String s = redisTemplate.opsForValue().get(uuid);
            if (s == null){
                throw new BusinessException(401, "Login Expired.");
            }else {
                ObjectMapper objectMapper = new ObjectMapper();
                UserResponseVo user = objectMapper.readValue(s, UserResponseVo.class);
                Long loginTime;
                if (user.getExpire_time() - (loginTime = System.currentTimeMillis()) < UserConstant.REFRESH_TOKEN_TIME * 60 * 1000){
                    user.setLogin_time(loginTime);
                    Long expireTime = loginTime + UserConstant.EXPIRE_TIME * 60 * 1000;
                    user.setExpire_time(expireTime);
                    String userAsString = objectMapper.writeValueAsString(user);
                    ValueOperations<String, String> ops = redisTemplate.opsForValue();
                    ops.set(uuid, userAsString, UserConstant.EXPIRE_TIME, TimeUnit.MINUTES);
                }
                loginUser.set(user);
            }
        }catch (Exception e){
            throw new BusinessException(401, "Please log in.");
        }
        return true;
    }
}
