package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("margin_account")
public class MarginAccount {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String masterChannelId;

    private String masterChannelName;

    private String accountName;

    private String bankAccount;

    /** 保证金首缴金额(元) */
    private BigDecimal marginFirstBalance;

    /** 充值保证金金额(元) */
    private BigDecimal rechargeBalance;

    /** 保证金留底金额(元) */
    private BigDecimal marginRemainBalance;

    /** 在途使用保证金(元) */
    private BigDecimal marginTransitUseBalance;

    /** 已退保证金(元) */
    private BigDecimal marginRefundedBalance;

    /** 冻结保证金(元) */
    private BigDecimal marginFrozenBalance;

    /** 待退款保证金(元) */
    private BigDecimal marginPendingRefundBalance;

    /** 状态: 0-审核中 1-已审核 3-关闭处理中 4-已关闭 */
    private String status;

    private String remark;

    private String createrId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    // ========== 计算字段（前端展示用）==========
    /** 关联渠道编码 */
    @TableField(exist = false)
    private String channelCodes;

    /** 关联渠道名称 */
    @TableField(exist = false)
    private String channelNames;

    /** 关联城市 */
    @TableField(exist = false)
    private String cityNames;

    /** 关联渠道列表 */
    @TableField(exist = false)
    private List<MarginAccountChannel> channelList;

    /** 可用保证金 */
    @TableField(exist = false)
    private BigDecimal kyBzjBalance;

    /** 待用保证金抵扣项 */
    @TableField(exist = false)
    private BigDecimal dybzjDeductionAmount;

    /** 占用在途保证金 */
    @TableField(exist = false)
    private BigDecimal zyztBzjBalance;

    /** 账面保证金余额 */
    @TableField(exist = false)
    private BigDecimal zmbzjAmount;

    /** 实际可退保证金 */
    @TableField(exist = false)
    private BigDecimal sjktAmount;

    /** 累扣保证金 */
    @TableField(exist = false)
    private BigDecimal marginFinishAmount;
}