package com.channel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.channel.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> selectUserRoles(Long userId);

    List<String> selectUserPermissions(Long userId);
}