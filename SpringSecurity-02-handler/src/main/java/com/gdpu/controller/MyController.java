package com.gdpu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @PreAuthorize("hasAuthority('sys:select')")
    @RequestMapping("/helloController")
    public String helloController(){
        return "helloController";
    }

    @PreAuthorize("hasAuthority('sys:select')")
    @RequestMapping("/selectController")
    public String selectController(){
        return "selectController";
    }
}
