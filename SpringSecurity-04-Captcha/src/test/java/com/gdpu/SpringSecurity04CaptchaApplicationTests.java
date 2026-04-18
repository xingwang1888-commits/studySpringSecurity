package com.gdpu;

import com.gdpu.entity.SysUser;
import com.gdpu.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurity04CaptchaApplicationTests {
    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    void selectTest() {
        SysUser sysUser = sysUserMapper.findByUsername("zhangsan");
        System.out.println(sysUser);

        List<String> permissions = sysUserMapper.findUserPermsByUserId(sysUser.getUserId());
        System.out.println(permissions);
    }

}
