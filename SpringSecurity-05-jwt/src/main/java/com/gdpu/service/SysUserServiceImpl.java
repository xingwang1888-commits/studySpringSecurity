package com.gdpu.service;

import com.gdpu.entity.SysUser;
import com.gdpu.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysUserServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser user = sysUserMapper.findByUserName(username);

        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        //获取用户权限
        List<String> permissions = sysUserMapper.findAuthenticationByUserId(user.getUserId());
        //将权限封装成SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authentication = new ArrayList<>();
        for (String permission : permissions){
            authentication.add(new SimpleGrantedAuthority(permission));
        }

        user.setPermissions(authentication);

        return user;

    }
}
