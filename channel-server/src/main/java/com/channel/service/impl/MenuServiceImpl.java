package com.channel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channel.entity.Menu;
import com.channel.mapper.MenuMapper;
import com.channel.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    @Cacheable(value = "menuTree")
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = lambdaQuery()
                .orderByAsc(Menu::getSortOrder)
                .list();
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<Menu> getUserMenus(Long userId) {
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        if (menus == null || menus.isEmpty()) {
            List<Menu> allMenus = lambdaQuery()
                    .orderByAsc(Menu::getSortOrder)
                    .list();
            return buildTree(allMenus, 0L);
        }
        return buildTree(menus, 0L);
    }

    private List<Menu> buildTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {
                    List<Menu> children = buildTree(menus, menu.getId());
                    menu.setChildren(children);
                    return menu;
                })
                .collect(Collectors.toList());
    }
}