package com.channelmargin.controller;

import com.channelmargin.dto.Result;
import com.channelmargin.entity.Menu;
import com.channelmargin.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/tree")
    public Result<List<Menu>> getTree() {
        return Result.success(menuService.getMenuTree());
    }

    @GetMapping("/list")
    public Result<List<Menu>> getList() {
        List<Menu> list = menuService.lambdaQuery()
                .orderByAsc(Menu::getSortOrder)
                .list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    @PostMapping
    @CacheEvict(value = {"menuTree", "userMenus"}, allEntries = true)
    public Result<Void> add(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success();
    }

    @PutMapping
    @CacheEvict(value = {"menuTree", "userMenus"}, allEntries = true)
    public Result<Void> update(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"menuTree", "userMenus"}, allEntries = true)
    public Result<Void> delete(@PathVariable Long id) {
        long count = menuService.lambdaQuery().eq(Menu::getParentId, id).count();
        if (count > 0) {
            return Result.error("瀛樺湪瀛愯彍鍗曪紝鏃犳硶鍒犻櫎");
        }
        menuService.removeById(id);
        return Result.success();
    }
}