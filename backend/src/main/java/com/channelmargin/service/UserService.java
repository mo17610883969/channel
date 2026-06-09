package com.channelmargin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.channelmargin.dto.LoginRequest;
import com.channelmargin.dto.LoginResponse;
import com.channelmargin.entity.User;

public interface UserService extends IService<User> {

    LoginResponse login(LoginRequest request);

    User getByUsername(String username);

    Page<User> pageUsers(int pageNum, int pageSize, String keyword);
}