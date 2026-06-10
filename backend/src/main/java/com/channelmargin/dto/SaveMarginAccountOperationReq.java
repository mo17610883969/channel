package com.channelmargin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 保存账户使用请求
 */
@Data
public class SaveMarginAccountOperationReq {
    private Long marginAccountId;
    private String masterChannelName;
    private String bussNo;
    private String custId;
    private String custName;
    private String contractNo;
    private String code;
    private String name;
    private BigDecimal loanAmt;
    private BigDecimal allocationRatio;
    private BigDecimal marginUseBalance;
    private String type;
    private String remark;
}