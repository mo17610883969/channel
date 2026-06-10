package com.channelmargin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.channelmargin.entity.MarginAccountOperationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MarginAccountOperationRecordMapper extends BaseMapper<MarginAccountOperationRecord> {

    /**
     * 分页查询账户使用列表
     */
    IPage<MarginAccountOperationRecord> selectUseListPage(Page<?> page, @Param("params") Map<String, Object> params);

    /**
     * 根据参数查询列表
     */
    List<MarginAccountOperationRecord> selectListByParams(@Param("params") Map<String, Object> params);

    /**
     * 获取在途使用的保证金金额
     */
    BigDecimal getMarginTransitUseBalance(@Param("marginAccountId") Long marginAccountId);

    /**
     * 检查订单号是否存在使用记录
     */
    long countByBussNo(@Param("bussNo") String bussNo);
}