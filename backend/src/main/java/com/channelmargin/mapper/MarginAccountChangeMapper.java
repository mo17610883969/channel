package com.channelmargin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.channelmargin.entity.MarginAccountChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MarginAccountChangeMapper extends BaseMapper<MarginAccountChange> {

    int updateStatusById(@Param("id") Long id, @Param("status") String status);
}