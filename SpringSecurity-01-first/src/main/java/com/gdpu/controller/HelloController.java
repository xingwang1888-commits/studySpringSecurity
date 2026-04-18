package com.gdpu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // 权限管理,只有角色拥有sys:save权限才能访问
    @PreAuthorize("hasAuthority('sys:save')")
    @RequestMapping("/hello")
    public String hello(){
        return "hello world, authority: save";
    }

    @PreAuthorize("hasAuthority('sys:select')")
    @RequestMapping("/select")
    public String select(){
        return "select, authority: select";
    }
}
