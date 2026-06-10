package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("margin_account_operation_record")
public class MarginAccountOperationRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long marginAccountId;

    /** 操作类型: SAVE-新增 UPDATE-修改 RECHARGE-充值 REFUND-退款 CLOSE-关闭 APPROVE-审核 */
    private String operateType;

    private String operateDesc;

    /** 操作详情(JSON) */
    private String operateDetail;

    private String operaterId;

    private String operaterName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}