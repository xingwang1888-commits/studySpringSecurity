package com.gdpu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/login")
    public String hello(){
        return "login";
    }

    @PreAuthorize("hasAuthority('sys:select')")
    @GetMapping("/select")
    public String select(){
        return "select";
    }
}
