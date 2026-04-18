package com.gdpu;

import com.gdpu.entity.SysUser;
import com.gdpu.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurityApplicationTests {

    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    void MybatisTest() {
        SysUser sysUser = sysUserMapper.findByUserName("zhangsan");
        System.out.println(sysUser);
        List<String> permissions = sysUserMapper.findAuthenticationByUserId(sysUser.getUserId());
        System.out.println(permissions);
    }

}
