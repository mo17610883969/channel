package com.channelmargin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新账户使用请求
 */
@Data
public class UpdateMarginAccountOperationReq {
    private Long marginAccountOperationRecordId;
    private String bussNo;
    private String custId;
    private String custName;
    private String contractNo;
    private BigDecimal loanAmt;
    private BigDecimal allocationRatio;
    private BigDecimal marginUseBalance;
    private String remark;
}