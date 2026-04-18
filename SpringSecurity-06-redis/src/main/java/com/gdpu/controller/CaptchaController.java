package com.gdpu.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/captcha")
    public void captchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建CircleCaptcha对象
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 10);

        //将captcha放在session中
        request.getSession().setAttribute("code", captcha.getCode());

        //用流传送到前端
        ImageIO.write(captcha.getImage(), "PNG", response.getOutputStream());
    }
}
