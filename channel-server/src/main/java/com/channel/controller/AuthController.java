package com.channel.controller;

import com.channel.dto.LoginRequest;
import com.channel.dto.LoginResponse;
import com.channel.dto.Result;
import com.channel.entity.Menu;
import com.channel.entity.User;
import com.channel.mapper.UserMapper;
import com.channel.service.MenuService;
import com.channel.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final MenuService menuService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return Result.success("登录成功", response);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/userinfo")
    public Result<LoginResponse> userInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        List<String> roles = userMapper.selectUserRoles(userId);

        LoginResponse response = LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .roles(roles)
                .build();
        return Result.success(response);
    }

    @GetMapping("/menus")
    public Result<List<Menu>> getUserMenus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Menu> menus = menuService.getUserMenus(userId);
        return Result.success(menus);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }
}