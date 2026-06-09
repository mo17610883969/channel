package com.channelmargin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.channelmargin.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectMenusByUserId(Long userId);
}