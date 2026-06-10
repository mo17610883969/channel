package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保证金账户渠道关联
 */
@Data
@TableName("margin_account_channel")
public class MarginAccountChannel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 保证金账户ID */
    private Long marginAccountId;

    /** 渠道ID */
    private Long channelId;

    /** 渠道编码 */
    private String code;

    /** 渠道名称 */
    private String name;

    /** 城市区域 */
    private String cityArea;

    /** 保证金首缴金额 */
    private BigDecimal marginFirstBalance;

    /** 充值金额 */
    private BigDecimal rechargeBalance;

    /** 在途使用保证金 */
    private BigDecimal marginTransitUseBalance;

    /** 已退款保证金 */
    private BigDecimal marginRefundedBalance;

    /** 状态: 0-禁用 1-启用 */
    private String status;

    /** 备注 */
    private String remark;

    /** 创建人ID */
    private String createrId;

    /** 创建人 */
    private String creater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新人 */
    private String editor;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @TableLogic
    private Integer deleted;
}