package com.gdpu.filter;

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

        //获取请求路径
        String requestURL = request.getRequestURI();
        //如果是登录请求
        if (requestURL.equals("/login/doLogin")){
            //获取session
            HttpSession session = request.getSession();
            String captchaCode = (String) session.getAttribute("code");
            //获取用户输入的code验证码
            String code = request.getParameter("code");
            if (!captchaCode.equalsIgnoreCase(code)) {
                //验证码错误
                Result result = new Result("500", "验证码错误");
                String json = objectMapper.writeValueAsString(result);
                response.getWriter().write(json);
                //阻止进行
                return;
            }

        }
        //放行
        filterChain.doFilter(request, response);
    }
}
