package com.gdpu.config;

import com.gdpu.handler.AppAccessDeniedHandler;
import com.gdpu.handler.AppAuthenticationFailureHandler;
import com.gdpu.handler.AppAuthenticationSuccessHandler;
import com.gdpu.handler.AppLogoutSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class HandlerConfiguration {

    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppLogoutSuccessHandler appLogoutSuccessHandler;

    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;
    @Resource
    private AppAccessDeniedHandler appAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //配置没有权限的handler
        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(appAccessDeniedHandler));

        //配置登录成功或失败handler
        httpSecurity.formLogin(handler -> handler.successHandler(appAuthenticationSuccessHandler).failureHandler(appAuthenticationFailureHandler));

        //配置登出成功handler
        httpSecurity.logout(handler -> handler.logoutSuccessHandler(appLogoutSuccessHandler));

        return httpSecurity.build();

    }
}
