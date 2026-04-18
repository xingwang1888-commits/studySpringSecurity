package com.gdpu.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Login Success");

        //设置相应字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        //返回结果Result
        Result result = new Result("200", "登录成功", authentication);

        //转为json
        String jsonStr = objectMapper.writeValueAsString(result);
        //输出给前端
        response.getWriter().write(jsonStr);
    }
}
