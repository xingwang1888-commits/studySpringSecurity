package com.gdpu.handler;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.entity.SysUser;
import com.gdpu.utils.JWTUtils;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //设置字符编码
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //返回Result
        Result result = new Result(200, "Login success");
        //获取用户信息
        SysUser user = (SysUser) authentication.getPrincipal();
        List<SimpleGrantedAuthority> authorities = user.getAuthorities();
        List<String> permissions = new ArrayList<>();
        for(SimpleGrantedAuthority authority:authorities){
            permissions.add(authority.getAuthority());
        }
        //创建JWT
        String jwt = JWTUtils.createJWT(user.getUserId(), user.getUsername(), permissions);
        result.setData(jwt);
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        //设置截止时间为1小时
        stringStringValueOperations.set(user.getUsername(), jwt, 60 * 60 * 1000, TimeUnit.SECONDS);
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}
