package com.channel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.channel.dto.LoginRequest;
import com.channel.dto.LoginResponse;
import com.channel.entity.User;

public interface UserService extends IService<User> {

    LoginResponse login(LoginRequest request);

    User getByUsername(String username);

    Page<User> pageUsers(int pageNum, int pageSize, String keyword);
}