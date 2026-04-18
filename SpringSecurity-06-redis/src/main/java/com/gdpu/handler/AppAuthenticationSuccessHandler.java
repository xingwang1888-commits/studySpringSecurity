package com.gdpu.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.entity.SysUser;
import com.gdpu.utils.JwtUtils;
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
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        //响应数据
        Result result = new Result("200", "登录成功", null);

        //获取用户信息
        SysUser user = (SysUser) authentication.getPrincipal();
        List<SimpleGrantedAuthority> permissions = user.getAuthorities();
        //将权限从SimpleGrantedAuthority转为String
        List<String> authorities = new ArrayList<>();
        for(SimpleGrantedAuthority p : permissions){
            authorities.add(String.valueOf(p));
        }

        //创建JWT
        String jwt = JwtUtils.createJWT(user.getUserId(), user.getUsername(), authorities);
        //响应数据
        result.setData(jwt);
        //将jwt存入redis中
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String Key = user.getUsername();
        //设置一天的过期时间
        stringStringValueOperations.set(Key, objectMapper.writeValueAsString(user), 24*60*60, TimeUnit.SECONDS);


        //转为json
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
