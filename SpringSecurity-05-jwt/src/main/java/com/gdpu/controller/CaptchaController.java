package com.gdpu.controller;

import cn.hutool.captcha.CircleCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
public class CaptchaController {

    @GetMapping("/code/image")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置captcha的长宽高
        CircleCaptcha captcha = new CircleCaptcha(100, 40, 4, 20);
        //将captcha放在session中
        request.getSession().setAttribute("code", captcha.getCode());
        //输出到前端
        ImageIO.write(captcha.getImage(), "PNG", response.getOutputStream());

    }
}
