package com.gdpu.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/captcha")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //生成验证码对象
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40, 4, 10);
        //将验证码放在session中
        request.getSession().setAttribute("code",captcha.getCode());
        //输出验证码到前端
        ImageIO.write(captcha.getImage(),"png",response.getOutputStream());
    }
}
