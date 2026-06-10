package com.channelmargin.dto;

import lombok.Data;

/**
 * 提交释放请求
 */
@Data
public class SubmitReleaseReq {
    private Long operationId;
    private String remark;
    private String type;
}