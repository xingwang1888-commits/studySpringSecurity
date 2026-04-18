package com.gdpu.mapper;

import com.gdpu.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author rainerw
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2026-04-08 22:40:50
* @Entity com.gdpu.entity.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    //根据用户名查询用户
    SysUser findByUserName(String username);

    //根据用户id查询用户权限
    List<String> findRoleByUserId(Integer userId);
}




