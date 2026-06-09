package com.channelmargin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.channelmargin.dto.Result;
import com.channelmargin.entity.User;
import com.channelmargin.mapper.UserMapper;
import com.channelmargin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/page")
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(userService.pageUsers(pageNum, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    @CacheEvict(value = "user", allEntries = true)
    public Result<Void> add(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        userService.save(user);
        return Result.success();
    }

    @PutMapping
    @CacheEvict(value = "user", allEntries = true)
    public Result<Void> update(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "user", allEntries = true)
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    @PutMapping("/reset-password")
    @CacheEvict(value = "user", allEntries = true)
    public Result<Void> resetPassword(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        User user = new User();
        user.setId(userId);
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        userService.updateById(user);
        return Result.success();
    }

    @GetMapping("/roles/{userId}")
    public Result<List<String>> getUserRoles(@PathVariable Long userId) {
        return Result.success(userMapper.selectUserRoles(userId));
    }
}