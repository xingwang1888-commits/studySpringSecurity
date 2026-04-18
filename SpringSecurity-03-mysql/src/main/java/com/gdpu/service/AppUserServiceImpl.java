package com.gdpu.service;

import com.gdpu.entity.sys_user;
import com.gdpu.mapper.sys_userMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppUserServiceImpl implements UserDetailsService {

    @Resource
    private sys_userMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        sys_user user = userMapper.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        //查询用户对应的权限
        List<String> p = userMapper.findPermissionsByUserId(user.getUserId());

        //转为SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String permission : p) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }

        user.setPermissions(authorities);

        return user;
    }
}
