package com.gdpu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.gdpu.mapper")
public class SpringSecurity07AllApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity07AllApplication.class, args);
    }

}
