package com.gdpu;

import com.gdpu.entity.SysUser;
import com.gdpu.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurity07AllApplicationTests {

    @Resource
    private SysUserMapper sysUserMapper;
    @Test
    void SQLTest() {
        System.out.println("SQL注入测试");
        SysUser sysUser = sysUserMapper.findByUserName("zhangsan");
        System.out.println(sysUser);
        List<String> roles = sysUserMapper.findRoleByUserId(sysUser.getUserId());
        System.out.println(roles);
    }

}
