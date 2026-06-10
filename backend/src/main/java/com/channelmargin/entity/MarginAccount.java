package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 保证金账户
 */
@Data
@TableName("margin_account")
public class MarginAccount {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 主渠道ID */
    private String masterChannelId;

    /** 主渠道名称 */
    private String masterChannelName;

    /** 账户名称 */
    private String accountName;

    /** 银行账号 */
    private String bankAccount;

    /** 保证金首缴金额（单位：元） */
    private BigDecimal marginFirstBalance;

    /** 充值金额（单位：元） */
    private BigDecimal rechargeBalance;

    /** 保证金留底金额（单位：元） */
    private BigDecimal marginRemainBalance;

    /** 在途使用保证金（单位：元） */
    private BigDecimal marginTransitUseBalance;

    /** 已退款保证金（单位：元） */
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

    // ========== 以下为非数据库字段，仅用于前端展示 ==========

    /** 关联的渠道编码（逗号分隔） */
    @TableField(exist = false)
    private String channelCodes;

    /** 关联的渠道名称（逗号分隔） */
    @TableField(exist = false)
    private String channelNames;

    /** 关联的城市名称（逗号分隔） */
    @TableField(exist = false)
    private String cityNames;

    /** 关联的渠道列表 */
    @TableField(exist = false)
    private List<MarginAccountChannel> channelList;

    /** 可用保证金余额（计算字段，单位：万元） */
    @TableField(exist = false)
    private BigDecimal kyBzjBalance;

    /** 账面保证金金额（计算字段，单位：万元） */
    @TableField(exist = false)
    private BigDecimal zmbzjAmount;

    /** 待退保证金（计算字段，单位：万元） */
    @TableField(exist = false)
    private BigDecimal marginPendingRefundBalance;

    /** 实际可退金额（计算字段） */
    @TableField(exist = false)
    private BigDecimal sjktAmount;

    /** 已完成保证金金额（计算字段） */
    @TableField(exist = false)
    private BigDecimal marginFinishAmount;
}