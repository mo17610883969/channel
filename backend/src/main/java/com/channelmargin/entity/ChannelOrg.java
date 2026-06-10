package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 渠道信息实体
 */
@Data
@TableName("channel_org")
public class ChannelOrg {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 渠道编码
     */
    private String code;

    /**
     * 渠道简称
     */
    private String name;

    /**
     * 渠道全称
     */
    private String fullName;

    /**
     * 类型 0-机构 1-经纪人
     */
    private String type;

    /**
     * 分类 0-房贷 1-教育贷
     */
    private String category;

    /**
     * 状态 0-可用 1-不可用
     */
    private String able;

    /**
     * 是否在回收站 0-正常 1-在回收站
     */
    private String recycle;

    /**
     * 成立时间
     */
    private LocalDateTime setUpTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 激活时间/签约时间
     */
    private LocalDateTime signTime;

    /**
     * 区域代码
     */
    private String area;

    /**
     * 城市区域
     */
    private String cityArea;

    /**
     * 通讯地址
     */
    private String communicationAddress;

    /**
     * 渠道等级
     */
    private String level;

    /**
     * 注册资金
     */
    private String registeCapital;

    /**
     * 公司规模
     */
    private String firmScale;

    /**
     * 备注
     */
    private String comment;

    /**
     * 开户银行
     */
    private String openBank;

    /**
     * 户名
     */
    private String account;

    /**
     * 证件号码
     */
    private String idcard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 渠道经理ID
     */
    private String owner;

    /**
     * 渠道经理姓名
     */
    private String ownerName;

    /**
     * 付款开户行
     */
    private String payOpenBank;

    /**
     * 付款户名
     */
    private String payAccount;

    /**
     * 付款卡号
     */
    private String payBankAccount;

    /**
     * 父级渠道编号
     */
    private String parentChannelId;

    /**
     * 渠道等级 1-一级渠道 2-二级渠道
     */
    private String channelLevel;

    /**
     * 展业模式
     */
    private String exhibition;

    /**
     * 展业模式说明
     */
    private String exhibitionRemark;

    /**
     * 主营业务
     */
    private String majorBusiness;

    /**
     * 主营业务说明
     */
    private String majorBusinessRemark;

    /**
     * 业务覆盖城市
     */
    private String majorBusinessCity;

    /**
     * 业务覆盖城市代码
     */
    private String majorBusinessCityCode;

    /**
     * 详细地址
     */
    private String detailAddr;

    /**
     * 代收代返 0-支持 1-拒绝
     */
    private String collectAndReturn;

    /**
     * 渠道属性 1-个人 2-机构
     */
    private String channelAttribute;

    /**
     * 配资标识 1-是 0-否
     */
    private String capitalAllocation;

    /**
     * 印章ID
     */
    private String sealId;

    /**
     * 印章图片
     */
    private String sealPhoto;

    /**
     * 是否大B 0-普通渠道 1-大B
     */
    private String isHunter;

    /**
     * 操作人
     */
    private String operater;

    /**
     * 操作人姓名
     */
    private String operaterName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}