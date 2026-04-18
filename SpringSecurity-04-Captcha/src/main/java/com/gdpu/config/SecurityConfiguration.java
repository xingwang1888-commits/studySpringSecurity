package com.gdpu.config;

import com.gdpu.filter.CaptchaFilter;
import com.gdpu.handler.AppAccessDeniedHandler;
import com.gdpu.handler.AppAuthenticationFailureHandler;
import com.gdpu.handler.AppAuthenticationSuccessHandler;
import com.gdpu.handler.AppLogOutSuccessHandler;
import com.gdpu.sevice.SysUserServiceImpl;
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
    private AppAccessDeniedHandler appAccessDeniedHandler;
    @Resource
    private AppLogOutSuccessHandler appLogOutSuccessHandler;
    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;
    @Resource
    private SysUserServiceImpl appUserService;
    @Resource
    private CaptchaFilter captchaFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.userDetailsService(appUserService);

        //在检验用户名和密码之前执行
        httpSecurity.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(appAccessDeniedHandler));

        httpSecurity.formLogin(handler -> handler.
                usernameParameter("uname")
                .passwordParameter("pwd")
                .loginPage("/toLogin")
                .loginProcessingUrl("/login/doLogin")
                .successHandler(appAuthenticationSuccessHandler)
                .failureHandler(appAuthenticationFailureHandler));

        httpSecurity.logout(handler -> handler.logoutSuccessHandler(appLogOutSuccessHandler));

        //关闭crsf
        httpSecurity.csrf(csrf -> csrf.disable());
        //允许跨域
        httpSecurity.cors(cors -> cors.disable());
        return httpSecurity.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
