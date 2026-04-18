package com.gdpu.config;

import com.gdpu.filter.CaptchaFilter;
import com.gdpu.filter.JwtFilter;
import com.gdpu.handler.AppAccessDeniedHandler;
import com.gdpu.handler.AppAuthenticationFailureHandler;
import com.gdpu.handler.AppAuthenticationSuccessHandler;
import com.gdpu.handler.AppLogOutSuccessHandler;
import com.gdpu.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HttpServletBean;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Resource
    private AppAccessDeniedHandler appAccessDeniedHandler;
    @Resource
    private AppLogOutSuccessHandler appLogOutSuccessHandler;
    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;
    @Resource
    private SysUserService SysUserService;
    @Resource
    private CaptchaFilter captchaFilter;
    @Resource
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HttpServletBean httpServletBean) throws Exception {

        httpSecurity.userDetailsService(SysUserService);

        httpSecurity.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(appAccessDeniedHandler));

        httpSecurity.formLogin(handler -> handler.successHandler(appAuthenticationSuccessHandler)
                .loginPage("/login")
                .loginProcessingUrl("/login/doLogin")
                .usernameParameter("uname")
                .passwordParameter("pwd")
                .failureHandler(appAuthenticationFailureHandler));

        httpSecurity.logout(handler -> handler.logoutSuccessHandler(appLogOutSuccessHandler));

        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers("/login", "/login/doLogin", "/captcha").permitAll().anyRequest().authenticated());

        //关闭csrf
        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
