package com.gdpu.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.entity.SysUser;
import com.gdpu.utils.JwtUtils;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;


    /**
     * security在登录成功之后会调用这个方法
     * 返回登录成功的提示信息，json格式
     * 生成jwt放入用户信息
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        //-------------新增开始-------------
        //获取用户信息
        SysUser user = (SysUser) authentication.getPrincipal();
        //将权限从SimpleGrantedAuthority转为String
        List<String> authorities = new ArrayList<>();
        for(SimpleGrantedAuthority authority : user.getAuthorities()){
            authorities.add(authority.getAuthority());
        }

        String jwt = JwtUtils.createJWT(user.getUserId(), user.getUsername(), authorities);

        //返回result
        Result result = new Result("200", "Login success");

        //转为json
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}
