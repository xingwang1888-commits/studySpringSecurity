package com.gdpu.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.entity.SysUser;
import com.gdpu.utils.JwtUtils;
import com.gdpu.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset:utf-8");

        //获取URL
        String requestURL = request.getRequestURI();
        if (requestURL.equals("/login") || requestURL.equals("/captcha") || requestURL.equals("/login/doLogin")) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //获取jwt
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            //设置Result
            Result result = new Result("500", "请先登录");
            //转json
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

        //截取jwt
        String jwt = authorization.replace("Bearer ", "");
        if (!JwtUtils.verifyJWT(jwt)) {
            //设置Result
            Result result = new Result("500", "请先登录");
            //转json
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

        //检查redis中是否有jwt
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String Key = JwtUtils.getUsername(jwt);
        String jwtKey = null;
        if (Key != null) {
            jwtKey = stringStringValueOperations.get(Key);
        }
        if (jwtKey != null) {
            //获取用户信息， 创建用户对象， 放入security
            String userId = JwtUtils.getUserId(jwt);
            String username = JwtUtils.getUsername(jwt);
            List<String> authority = JwtUtils.getAuthority(jwt);

            //转换
            List<SimpleGrantedAuthority> permissions = new ArrayList<>();
            if (authority != null) {
                for (String s : authority) {
                    permissions.add(new SimpleGrantedAuthority(s));
                }
            }


            SysUser user = new SysUser();

            if (userId != null) {
                user.setUserId(Integer.parseInt(userId));
            }

            user.setUsername(username);

            user.setAuthorities(permissions);
            //将SysUser填入security中,security会认为当前认证成功
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, permissions);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
            filterChain.doFilter(request, response);
            return;
        }else{
            //设置Result
            Result result = new Result("500", "请先登录");
            //转json
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }
    }
}
