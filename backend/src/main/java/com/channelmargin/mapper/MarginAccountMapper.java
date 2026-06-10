package com.channelmargin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.channelmargin.entity.MarginAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface MarginAccountMapper extends BaseMapper<MarginAccount> {

    /**
     * 更新在途使用保证金金额（增加）
     */
    int updateMarginTransitUseBalanceById(@Param("id") Long id, @Param("amount") BigDecimal amount);

    /**
     * 更新在途使用保证金金额（减少）
     */
    int updateSubMarginTransitUseBalanceById(@Param("id") Long id, @Param("amount") BigDecimal amount);
}