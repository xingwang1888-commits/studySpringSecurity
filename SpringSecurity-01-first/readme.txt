1.体验SpringSecurity
只需要写controller文件即可，运行程序时会有一串密码，用户名为user， 输入密码即可访问你的网页
2.自定义username and password
(方法一)
spring:
    security:
        user:
            username:
            password:


(方法二)
创建认证的配置类
package com.gdpu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration {

    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails admin = User.builder().username("admin").password("123456").authorities("sys:save", "sys:select").build();

        UserDetails jackson = User.builder().username("jackson").password("123456").authorities("sys:select").build();

        return new InMemoryUserDetailsManager(admin, jackson);
    }
}

只要添加了安全配置类，那么我们在yml里面的配置就失效了
我们使用jackson/123456访问登录，发现控制台报错了
这个是因为spring Security强制要使用密码加密，当然我们也可以不加密，但是官方要求是不管你是否加密，都必须配置一个类似Shiro的凭证匹配器

(方法二完整代码)
package com.gdpu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)    //开启security授权
public class SecurityConfiguration {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(BCryptPasswordEncoder passwordEncoder) {
        String encode = passwordEncoder.encode("123456");

        UserDetails admin = User.builder().username("admin").password(encode).authorities("sys:save", "sys:select").build();

        UserDetails jackson = User.builder().username("jackson").password(encode).authorities("sys:select").build();

        return new InMemoryUserDetailsManager(admin, jackson);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
验证原文是否正确：

bcrypt是一种单向哈希函数，不支持解密。它的设计目的是防止从哈希值反推出原始密码。当需要验证用户输入的密码时，可以使用以下步骤：

原文：123

密文 $2a$10$8Vx2N9K2MJ3/7JfzFYTfheJ470j0099gcefdpRh9FO7XMApnIpDHC

hash( 原文 + 随机盐值     )    生成密文

1. 从存储的密码哈希值中获取盐值和工作因子。
2. 使用相同的工作因子和盐值，将用户输入的密码进行哈希计算。
3. 将计算得到的哈希值与存储的密码哈希值进行比较。如果两者相同，说明密码匹配；如果不同，则密码不匹配。



两种加密方式

- 对称加密      原文 + 加密    发信者和收信者都知道     java入门  18 20 3
- 非对称加密    公钥   验钞机     私钥    印钞机    更安全