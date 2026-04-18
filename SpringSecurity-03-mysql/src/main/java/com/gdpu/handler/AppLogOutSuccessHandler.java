package com.gdpu.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AppLogOutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    ObjectMapper objectMapper;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        Result result = new Result(200, "注销成功");

        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
