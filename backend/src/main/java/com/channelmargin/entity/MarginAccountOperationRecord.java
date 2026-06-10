package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保证金账户使用记录
 */
@Data
@TableName("margin_account_operation_record")
public class MarginAccountOperationRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 类型: USE-使用 RELEASE-释放 */
    private String type;

    /** 关联的使用记录ID（释放时关联） */
    private Long operationRecordId;

    /** 保证金账户ID */
    private Long marginAccountId;

    /** 主渠道名称 */
    private String masterChannelName;

    /** 账户关联ID */
    private Long marginAccountChannelId;

    /** 业务单号 */
    private String bussNo;

    /** 客户姓名 */
    private String custName;

    /** 客户ID */
    private String custId;

    /** 合同号 */
    private String contractNo;

    /** 渠道编码 */
    private String code;

    /** 渠道名称 */
    private String name;

    /** 合同金额 */
    private BigDecimal contAmt;

    /** 配资比例 */
    private BigDecimal allocationRatio;

    /** 使用保证金金额 */
    private BigDecimal marginUseBalance;

    /** 状态: 0-待审核 1-审核通过/使用中 2-审核拒绝 3-释放审核中 4-已释放 */
    private String status;

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

    /** 城市区域 */
    private String cityArea;

    /** 订单状态 */
    private String orderStatus;

    /** 备注 */
    private String remark;

    @TableLogic
    private Integer deleted;
}
