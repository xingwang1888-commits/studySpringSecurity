package com.gdpu.config;


import com.gdpu.filter.CaptchaFilter;
import com.gdpu.filter.JWTCheckFilter;
import com.gdpu.handler.AppAccessDeniedHandler;
import com.gdpu.handler.AppAuthenticationFailureHandler;
import com.gdpu.handler.AppAuthenticationSuccessHandler;
import com.gdpu.handler.AppLogOutSuccessHandler;
import com.gdpu.service.SysUserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;
    @Resource
    private AppLogOutSuccessHandler appLogOutSuccessHandler;
    @Resource
    private AppAccessDeniedHandler appAccessDeniedHandler;
    @Resource
    private SysUserServiceImpl sysUserService;
    @Resource
    private CaptchaFilter captchaFilter;
    @Resource
    private JWTCheckFilter jwtCheckFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.userDetailsService(sysUserService);

        //在验证用户名和密码之前
        httpSecurity.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);


        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(appAccessDeniedHandler));

        httpSecurity.formLogin(handler -> handler
                .usernameParameter("uname")
                .passwordParameter("pwd")
                .loginPage("/toLogin")
                .loginProcessingUrl("/login/doLogin")
                .successHandler(appAuthenticationSuccessHandler)
                .failureHandler(appAuthenticationFailureHandler));

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/toLogin", "/login/doLogin", "/logout", "code/image").
                permitAll().anyRequest().authenticated());

        httpSecurity.logout(handler -> handler.logoutSuccessHandler(appLogOutSuccessHandler));

        //关闭csrf
        httpSecurity.csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
