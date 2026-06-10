package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保证金账户变更记录
 */
@Data
@TableName("margin_account_change")
public class MarginAccountChange {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 保证金账户ID */
    private Long marginAccountId;

    /** 变更类型: USE-使用 FREEZE-冻结 UNFREEZE-解冻 REFUND-退款 RECHARGE-充值 */
    private String changeType;

    /** 变更金额 */
    private BigDecimal changeAmount;

    /** 变更前余额 */
    private BigDecimal beforeBalance;

    /** 变更后余额 */
    private BigDecimal afterBalance;

    /** 关联业务ID */
    private Long businessId;

    /** 关联业务单号 */
    private String businessNo;

    /** 变更原因 */
    private String reason;

    /** 操作人ID */
    private String operaterId;

    /** 操作人 */
    private String operater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}