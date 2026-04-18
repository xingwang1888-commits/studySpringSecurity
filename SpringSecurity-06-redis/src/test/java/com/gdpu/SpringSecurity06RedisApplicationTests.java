package com.gdpu;

import com.gdpu.entity.SysUser;
import com.gdpu.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurity06RedisApplicationTests {

    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    void SysUserTest() {
        SysUser sysUser = sysUserMapper.findByUsername("zhangsan");
        System.out.println(sysUser);
        List<String> permissions = sysUserMapper.findPermissionsByUserId(sysUser.getUserId());
        System.out.println(permissions);
    }

}
