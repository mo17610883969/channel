package com.channelmargin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.channelmargin.dto.ApproveMarginAccountReq;
import com.channelmargin.dto.SaveMarginAccountOperationReq;
import com.channelmargin.dto.SubmitReleaseReq;
import com.channelmargin.dto.UpdateMarginAccountOperationReq;
import com.channelmargin.entity.MarginAccountOperationRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MarginAccountOperationRecordService extends IService<MarginAccountOperationRecord> {

    /**
     * 分页查询账户使用列表
     */
    IPage<MarginAccountOperationRecord> selectUseListPage(int current, int size, Map<String, Object> params);

    /**
     * 保存账户使用
     */
    Map<String, Object> saveMarginAccountOperation(SaveMarginAccountOperationReq req);

    /**
     * 更新账户使用
     */
    Map<String, Object> updateMarginAccountOperation(UpdateMarginAccountOperationReq req);

    /**
     * 获取详情
     */
    Map<String, Object> getDetails(Long id);

    /**
     * 审核使用
     */
    Map<String, Object> approve(ApproveMarginAccountReq req);

    /**
     * 审核释放
     */
    Map<String, Object> approveRelease(ApproveMarginAccountReq req);

    /**
     * 提交释放
     */
    Map<String, Object> submitRelease(SubmitReleaseReq req);

    /**
     * 获取在途使用的保证金金额
     */
    BigDecimal getMarginTransitUseBalance(Long marginAccountId);

    /**
     * 检查订单号是否存在使用记录
     */
    long getMarginAccountByBussNo(String bussNo);

    /**
     * 根据参数查询列表
     */
    List<MarginAccountOperationRecord> queryListByParam(Map<String, Object> param);
}
