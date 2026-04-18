package com.gdpu;

import com.gdpu.entity.sys_user;
import com.gdpu.mapper.sys_userMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurity03MysqlApplicationTests {
    @Resource
    private sys_userMapper userMapper;

    @Test
    void findUserByName() {
        sys_user zhangsan = userMapper.findByUsername("zhangsan");
        System.out.println(zhangsan);

    }

    @Test
    void findAuthenticationByUserIdTest() {
        List<String> permissions = userMapper.findPermissionsByUserId(1);
        System.out.println(permissions);
    }
}
