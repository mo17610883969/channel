-- 保证金账户表
CREATE TABLE IF NOT EXISTS margin_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    master_channel_id VARCHAR(50),
    master_channel_name VARCHAR(100),
    account_name VARCHAR(100),
    bank_account VARCHAR(50),
    margin_first_balance DECIMAL(15,2) DEFAULT 0,
    recharge_balance DECIMAL(15,2) DEFAULT 0,
    margin_remain_balance DECIMAL(15,2) DEFAULT 0,
    margin_transit_use_balance DECIMAL(15,2) DEFAULT 0,
    margin_refunded_balance DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(10) DEFAULT '1',
    remark VARCHAR(500),
    creater_id VARCHAR(50),
    creater VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    editor VARCHAR(50),
    edit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 保证金账户渠道关联表
CREATE TABLE IF NOT EXISTS margin_account_channel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    margin_account_id BIGINT,
    channel_id BIGINT,
    code VARCHAR(50),
    name VARCHAR(100),
    city_area VARCHAR(100),
    margin_first_balance DECIMAL(15,2) DEFAULT 0,
    recharge_balance DECIMAL(15,2) DEFAULT 0,
    margin_transit_use_balance DECIMAL(15,2) DEFAULT 0,
    margin_refunded_balance DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(10) DEFAULT '1',
    remark VARCHAR(500),
    creater_id VARCHAR(50),
    creater VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    editor VARCHAR(50),
    edit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 保证金账户变更记录表
CREATE TABLE IF NOT EXISTS margin_account_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    margin_account_id BIGINT,
    change_type VARCHAR(20),
    change_amount DECIMAL(15,2),
    before_balance DECIMAL(15,2),
    after_balance DECIMAL(15,2),
    business_id BIGINT,
    business_no VARCHAR(50),
    reason VARCHAR(500),
    operater_id VARCHAR(50),
    operater VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 保证金账户使用记录表
CREATE TABLE IF NOT EXISTS margin_account_operation_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    operation_record_id BIGINT,
    margin_account_id BIGINT,
    master_channel_name VARCHAR(100),
    margin_account_channel_id BIGINT,
    buss_no VARCHAR(50),
    cust_name VARCHAR(50),
    cust_id VARCHAR(50),
    contract_no VARCHAR(50),
    code VARCHAR(50),
    name VARCHAR(100),
    cont_amt DECIMAL(15,2),
    allocation_ratio DECIMAL(5,4),
    margin_use_balance DECIMAL(15,2),
    status VARCHAR(10) DEFAULT '0',
    creater_id VARCHAR(50),
    creater VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    editor VARCHAR(50),
    edit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    city_area VARCHAR(100),
    order_status VARCHAR(20),
    remark VARCHAR(500),
    deleted TINYINT DEFAULT 0
);

-- 插入示例数据
INSERT INTO margin_account (master_channel_name, account_name, margin_first_balance, recharge_balance, status) VALUES
('北京渠道总部', '北京保证金账户', 500000.00, 100000.00, '1'),
('上海渠道总部', '上海保证金账户', 300000.00, 50000.00, '1');

INSERT INTO margin_account_channel (margin_account_id, code, name, city_area, margin_first_balance, recharge_balance, status) VALUES
(1, 'BJ001', '北京分公司', '北京市', 500000.00, 100000.00, '1'),
(1, 'BJ002', '北京朝阳支公司', '北京市朝阳区', 200000.00, 30000.00, '1'),
(2, 'SH001', '上海分公司', '上海市', 300000.00, 50000.00, '1');

-- 插入账户使用示例数据
INSERT INTO margin_account_operation_record (type, margin_account_id, master_channel_name, buss_no, cust_name, contract_no, code, name, cont_amt, margin_use_balance, status, creater) VALUES
('USE', 1, '北京渠道总部', 'ORD20240601001', '张三', 'HT20240601001', 'BJ001', '北京分公司', 50000.00, 5000.00, '0', 'admin'),
('USE', 1, '北京渠道总部', 'ORD20240601002', '李四', 'HT20240601002', 'BJ001', '北京分公司', 80000.00, 8000.00, '1', 'admin'),
('USE', 2, '上海渠道总部', 'ORD20240601003', '王五', 'HT20240601003', 'SH001', '上海分公司', 60000.00, 6000.00, '2', 'admin');