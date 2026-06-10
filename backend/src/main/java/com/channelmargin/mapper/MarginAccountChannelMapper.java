package com.channelmargin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.channelmargin.entity.MarginAccountChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MarginAccountChannelMapper extends BaseMapper<MarginAccountChannel> {

    /**
     * 根据渠道编码查询
     */
    List<Map<String, Object>> selectByCode(@Param("code") String code);

    /**
     * 根据名称模糊查询账户ID列表
     */
    List<Long> selectAccountIdsByName(@Param("name") String name);
}