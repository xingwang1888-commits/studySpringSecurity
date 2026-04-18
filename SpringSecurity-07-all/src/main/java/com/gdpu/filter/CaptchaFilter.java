package com.gdpu.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

        //获取url
        String requestURL = request.getRequestURI();

        //拦截处理登录请求的url
        if(requestURL.equals("/login/doLogin")){
            //从session中获取验证码
            String captcha = (String) request.getSession().getAttribute("code");
            String inputCaptcha = request.getParameter("code");

            if(!captcha.equalsIgnoreCase(inputCaptcha)){
                Result result = new Result(-1, "验证码错误");

                String json = objectMapper.writeValueAsString(result);

                response.getWriter().write(json);

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
