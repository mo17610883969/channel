package com.channelmargin.dto;

import lombok.Data;

/**
 * 审核账户请求
 */
@Data
public class ApproveMarginAccountReq {
    private Long marginApproveId;
    private String firstApprStatus;
    private String firstApprDesc;
}