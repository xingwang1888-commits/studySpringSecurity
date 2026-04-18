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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class JWTCheckFilter extends OncePerRequestFilter {
    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        //获取请求路径
        String requestURL = request.getRequestURI();

        //如果是请求路径,则放行
        if(requestURL.equals("/login/doLogin")){
            filterChain.doFilter(request,response);
            return;
        }

        //获取前端发送的jwt
        //这里的authorization是：Bearer jwt
        String authorization = request.getHeader("Authorization");
        if(authorization == null){
            //响应错误信息
            Result result = new Result("500", "请先登录");
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }
        //提取jwt， 注意Bearer 后面有一个空格
        String jwt = authorization.replace("Bearer ", "");

        //检查jwt
        if(!JwtUtils.verifyJWT(jwt)){
            Result result = new Result("500", "请先登录");
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }

        //从Jwt中获取用户信息，构建用户对象， 将用户对象放入SecurityContext中
        String username = JwtUtils.getUsername(jwt);
        String userId = JwtUtils.getUserId(jwt);
        List<String> authority = JwtUtils.getAuthority(jwt);
        //将权限列表转换成SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (authority != null) {
            for(String s : authority){
                authorities.add(new SimpleGrantedAuthority(s));
            }
        }

        SysUser user = new SysUser();
        if (userId != null) {
            user.setUserId(Integer.parseInt(userId));
        }
        user.setUsername(username);
        user.setPermissions(authorities);

        //将SysUser填入security中,security会认为当前认证成功
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
