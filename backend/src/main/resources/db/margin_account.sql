-- 渠道保证金账户表
DROP TABLE IF EXISTS margin_account_change;
DROP TABLE IF EXISTS margin_account_operation_record;
DROP TABLE IF EXISTS margin_account_channel;
DROP TABLE IF EXISTS margin_account;

CREATE TABLE margin_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    master_channel_id VARCHAR(50) NOT NULL COMMENT '渠道主键ID',
    master_channel_name VARCHAR(100) NOT NULL COMMENT '渠道名称',
    account_name VARCHAR(100) COMMENT '开户名称',
    bank_account VARCHAR(50) COMMENT '银行账号',
    margin_first_balance DECIMAL(20,4) DEFAULT 0 COMMENT '保证金首缴金额(元)',
    recharge_balance DECIMAL(20,4) DEFAULT 0 COMMENT '充值保证金金额(元)',
    margin_remain_balance DECIMAL(20,4) DEFAULT 0 COMMENT '保证金留底金额(元)',
    margin_transit_use_balance DECIMAL(20,4) DEFAULT 0 COMMENT '在途使用保证金(元)',
    margin_refunded_balance DECIMAL(20,4) DEFAULT 0 COMMENT '已退保证金(元)',
    margin_frozen_balance DECIMAL(20,4) DEFAULT 0 COMMENT '冻结保证金(元)',
    margin_pending_refund_balance DECIMAL(20,4) DEFAULT 0 COMMENT '待退款保证金(元)',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态: 0-审核中 1-已审核 3-关闭处理中 4-已关闭',
    remark VARCHAR(500) COMMENT '备注',
    creater_id VARCHAR(50) COMMENT '创建人ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

-- 渠道保证金账户关联渠道表
CREATE TABLE margin_account_channel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    margin_account_id BIGINT NOT NULL COMMENT '保证金账户ID',
    channel_id BIGINT NOT NULL COMMENT '渠道ID',
    code VARCHAR(50) COMMENT '渠道编码',
    name VARCHAR(100) COMMENT '渠道名称',
    city_area VARCHAR(100) COMMENT '城市区域',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

-- 渠道保证金变动记录表
CREATE TABLE margin_account_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    margin_account_id BIGINT NOT NULL COMMENT '保证金账户ID',
    change_type VARCHAR(20) NOT NULL COMMENT '变动类型: RECHARGE-充值 REFUND-退款 CLOSE-关闭',
    margin_amount DECIMAL(20,4) NOT NULL COMMENT '变动金额(元)',
    balance_before DECIMAL(20,4) DEFAULT 0 COMMENT '变动前余额(元)',
    balance_after DECIMAL(20,4) DEFAULT 0 COMMENT '变动后余额(元)',
    remark VARCHAR(500) COMMENT '备注',
    image_urls TEXT COMMENT '凭证图片URLs(JSON数组)',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态: 0-待审核 1-已审核 2-已拒绝',
    approve_remark VARCHAR(500) COMMENT '审核备注',
    approver_id VARCHAR(50) COMMENT '审核人ID',
    approve_time TIMESTAMP COMMENT '审核时间',
    creater_id VARCHAR(50) COMMENT '创建人ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

-- 渠道保证金操作记录表
CREATE TABLE margin_account_operation_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    margin_account_id BIGINT NOT NULL COMMENT '保证金账户ID',
    operate_type VARCHAR(20) NOT NULL COMMENT '操作类型: SAVE-新增 UPDATE-修改 RECHARGE-充值 REFUND-退款 CLOSE-关闭 APPROVE-审核',
    operate_desc VARCHAR(255) COMMENT '操作描述',
    operate_detail TEXT COMMENT '操作详情(JSON)',
    operater_id VARCHAR(50) COMMENT '操作人ID',
    operater_name VARCHAR(50) COMMENT '操作人姓名',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);