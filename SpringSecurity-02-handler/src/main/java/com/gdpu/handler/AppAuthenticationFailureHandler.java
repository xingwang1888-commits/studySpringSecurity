package com.gdpu.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Resource
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("Login Failure");
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        //返回Result
        Result result = new Result("500", "登录失败");
        if(exception instanceof BadCredentialsException){
            result.setData("密码不正确");
        }else if(exception instanceof DisabledException){
            result.setData("账号被禁用");
        }else if(exception instanceof UsernameNotFoundException){
            result.setData("用户名不存在");
        }else if(exception instanceof CredentialsExpiredException){
            result.setData("密码已过期");
        }else if(exception instanceof AccountExpiredException){
            result.setData("账号已过期");
        }else if(exception instanceof LockedException){
            result.setData("账号被锁定");
        }else{
            result.setData("未知异常");
        }


        //转为json
        String jsonStr = objectMapper.writeValueAsString(result);
        //输出给前端
        response.getWriter().write(jsonStr);
    }
}
