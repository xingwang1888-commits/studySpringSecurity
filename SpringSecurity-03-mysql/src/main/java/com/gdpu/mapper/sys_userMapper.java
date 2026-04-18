package com.gdpu.mapper;

import com.gdpu.entity.sys_user;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author rainerw
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2026-03-27 17:20:31
* @Entity generator.entity.sys_user
*/
@Mapper
public interface sys_userMapper extends BaseMapper<sys_user> {
    /**
     * 根据用户名查询用户
     */
    sys_user findByUsername(String username);

    /**
     * 根据用户名id查询用户权限
     */
    List<String> findPermissionsByUserId(Integer id);
}




