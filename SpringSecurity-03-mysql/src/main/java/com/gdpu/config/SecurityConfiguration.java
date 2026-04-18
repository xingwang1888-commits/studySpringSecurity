package com.gdpu.config;

import com.gdpu.handler.AppAccessDeniedHandler;
import com.gdpu.handler.AppAuthenticationFailureHandler;
import com.gdpu.handler.AppAuthenticationSuccessHandler;
import com.gdpu.handler.AppLogOutSuccessHandler;
import com.gdpu.service.AppUserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Resource
    private AppAccessDeniedHandler appAccessDeniedHandler;
    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;
    @Resource
    private AppLogOutSuccessHandler appLogOutSuccessHandler;
    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppUserServiceImpl appUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.userDetailsService(appUserService);

        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(appAccessDeniedHandler));

        httpSecurity.formLogin(handler -> handler.
                usernameParameter("uname")   //默认获取的是username， 如果前端的变量是uname， 那么就使用uname
                .passwordParameter("pwd")  //默认获取的是password， 如果前端的变量是pwd， 那么就使用pwd
                .loginPage("/toLogin") //controller的login方法的地址进行登录
                .loginProcessingUrl("/login/doLogin")  //处理前端登录请求的地址，即表单提交地址
                .successHandler(appAuthenticationSuccessHandler).
                failureHandler(appAuthenticationFailureHandler));

        //放行登录等请求  不需要security验证token的地址写到这里
        //除了/toLogin /login/doLogin 之外的其他请求，都需要security验证token
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers("/toLogin", "/login/doLogin")
                        .permitAll()
                        .anyRequest().authenticated());

        //关闭csrf
        httpSecurity.csrf(csrf -> csrf.disable());
        //允许前端跨域访问
        //httpSecurity.cors(cors -> cors.disable());
        httpSecurity.logout(handler -> handler.logoutSuccessHandler(appLogOutSuccessHandler));

        return httpSecurity.build();
    }


    //配置密码加密
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
