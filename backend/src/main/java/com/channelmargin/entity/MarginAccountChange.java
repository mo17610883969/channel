package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("margin_account_change")
public class MarginAccountChange {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long marginAccountId;

    /** 变动类型: RECHARGE-充值 REFUND-退款 CLOSE-关闭 */
    private String changeType;

    /** 变动金额(元) */
    private BigDecimal marginAmount;

    /** 变动前余额(元) */
    private BigDecimal balanceBefore;

    /** 变动后余额(元) */
    private BigDecimal balanceAfter;

    private String remark;

    /** 凭证图片URLs(JSON数组) */
    private String imageUrls;

    /** 状态: 0-待审核 1-已审核 2-已拒绝 */
    private String status;

    private String approveRemark;

    private String approverId;

    private LocalDateTime approveTime;

    private String createrId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}