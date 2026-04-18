package com.gdpu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)    //开启security授权
public class SecurityConfiguration {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(BCryptPasswordEncoder passwordEncoder) {
        String encode = passwordEncoder.encode("123456");

        UserDetails admin = User.builder().username("admin").password(encode).authorities("sys:save", "sys:select").build();

        UserDetails jackson = User.builder().username("jackson").password(encode).authorities("sys:select").build();

        return new InMemoryUserDetailsManager(admin, jackson);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
