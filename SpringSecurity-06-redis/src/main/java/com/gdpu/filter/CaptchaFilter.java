package com.gdpu.filter;

import cn.hutool.db.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        //获取请求url
        String requestURL = request.getRequestURI();
        if (requestURL.equals("/login/doLogin")) {
            //获取session
            HttpSession session = request.getSession();
            //获取captchaCode
            String captchaCode = (String) session.getAttribute("code");
            //获取用户输入的captchaCode
            String inputCode = request.getParameter("code");
            if (!captchaCode.equalsIgnoreCase(inputCode)) {
                Result result = new Result("500", "验证码错误");
                String json = objectMapper.writeValueAsString(result);
                response.getWriter().write(json);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}
