package com.gdpu.sevice;


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
        SysUser user = sysUserMapper.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        //获取权限
        List<String> permissions = sysUserMapper.findUserPermsByUserId(user.getUserId());
        //封装为SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authentications = new ArrayList<>();
        for(String permission : permissions){
            authentications.add(new SimpleGrantedAuthority(permission));
        }

        user.setPermissions(authentications);

        return user;
    }
}
