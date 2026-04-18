package com.gdpu.controller;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/select")
    public String logout(){
        return "select";
    }
}
