package com.channel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.channel.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> getMenuTree();

    List<Menu> getUserMenus(Long userId);
}