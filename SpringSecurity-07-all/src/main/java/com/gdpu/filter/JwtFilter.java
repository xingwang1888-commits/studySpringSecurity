package com.gdpu.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdpu.entity.SysUser;
import com.gdpu.utils.JWTUtils;
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //获取url
        String requestURL = request.getRequestURI();
        if(requestURL.equals("/login") || requestURL.equals("/captcha") || requestURL.equals("/login/doLogin")){
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //获取jwt
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            Result result = new Result(401, "请先登录");
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

        //截取jwt
        String jwt = authorization.replace("Bearer ", "");
        if (!JWTUtils.verifyJWT(jwt)){
            Result result = new Result(401, "请先登录");
            //转json
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String username = JWTUtils.getUsername(jwt);
        String userId = JWTUtils.getUserId(jwt);
        List<String> authority = JWTUtils.getAuthority(jwt);
        List<SimpleGrantedAuthority> permissions = new ArrayList<>();
        if (authority != null) {
            for (String s : authority) {
                permissions.add(new SimpleGrantedAuthority(s));
            }
        }
        if(username != null){
            String redisJWT = valueOperations.get(username);
            if(redisJWT != null){
                if(!redisJWT.equals(jwt)){
                    Result result = new Result(401, "请先登录");
                    //转json
                    String json = objectMapper.writeValueAsString(result);
                    response.getWriter().write(json);
                    return;
                }

                SysUser user = new SysUser();
                user.setUsername(username);
                if (userId != null) {
                    user.setUserId(Integer.parseInt(userId));
                }
                user.setAuthorities(permissions);

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, permissions);
                SecurityContextHolder.getContext().setAuthentication(token);
                filterChain.doFilter(request, response);
                return;
            }else{
                Result result = new Result(401, "请先登录");
                String json = objectMapper.writeValueAsString(result);
                response.getWriter().write(json);
            }
        }else{
            Result result = new Result(401, "用户名不能为空");
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

    }
}
