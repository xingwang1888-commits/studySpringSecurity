package com.gdpu.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/code/image")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建一个验证码  长，宽，字符数，干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40, 4, 10);
        //将captcha放在session中
        request.getSession().setAttribute("code", captcha.getCode());
        //将captcha输出到浏览器
        ImageIO.write(captcha.getImage(), "PNG", response.getOutputStream());
    }
}
