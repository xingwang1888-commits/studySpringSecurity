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

        //获取URL请求
        String requestURL = request.getRequestURI();
        //如果是登录请求
        if(requestURL.equals("/login")){
            //获取session
            HttpSession session = request.getSession();
            //获取验证码code
            String captchaCode = (String) session.getAttribute("code");
            //获取用户输入的验证码
            String inputCode = request.getParameter("code");
            //如果验证码不一致
            if(!captchaCode.equalsIgnoreCase(inputCode)){
                Result result = new Result("-1", "验证码错误");
                String json = objectMapper.writeValueAsString(result);
                response.getWriter().write(json);
                //组织方法进行下去
                return;
            }
        }
        //放行
        filterChain.doFilter(request, response);
    }
}
