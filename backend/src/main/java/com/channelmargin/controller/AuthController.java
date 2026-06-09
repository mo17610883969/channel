package com.channelmargin.controller;

import com.channelmargin.dto.LoginRequest;
import com.channelmargin.dto.LoginResponse;
import com.channelmargin.dto.Result;
import com.channelmargin.entity.Menu;
import com.channelmargin.entity.User;
import com.channelmargin.mapper.UserMapper;
import com.channelmargin.service.MenuService;
import com.channelmargin.service.UserService;
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