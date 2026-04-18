captcha三步：
1.创建controller层（captchaController）
创建circleCaptcha->将captchaCode存储到Session中->使用ImageIO.write()发送到前端
2、创建captchaFilter
继承OncePerRequestFilter->设置字符编码->获取url->截获登录url->获取session中的captchaCode->获取用户输入的验证码->进行比对->不成功截获，成功放行
3.配置类
配置类添加httpSecurity.addFilterBefore(过滤器， UsernamePasswordAuthenticationFilter.class)
作用在密码跟用户名之前