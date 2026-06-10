DROP TABLE IF EXISTS sys_user_menu;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    menu_name VARCHAR(50) NOT NULL,
    menu_type VARCHAR(20) NOT NULL,
    path VARCHAR(255),
    component VARCHAR(255),
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    permission VARCHAR(100),
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);

CREATE TABLE sys_user_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL
);

-- 渠道信息表
CREATE TABLE channel_org (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '渠道编码',
    name VARCHAR(100) NOT NULL COMMENT '渠道简称',
    full_name VARCHAR(255) COMMENT '渠道全称',
    type VARCHAR(10) DEFAULT '0' COMMENT '类型 0-机构 1-经纪人',
    category VARCHAR(10) DEFAULT '0' COMMENT '分类 0-房贷 1-教育贷',
    able VARCHAR(10) DEFAULT '0' COMMENT '状态 0-可用 1-不可用',
    recycle VARCHAR(10) DEFAULT '0' COMMENT '是否在回收站 0-正常 1-在回收站',
    set_up_time TIMESTAMP COMMENT '成立时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    sign_time TIMESTAMP COMMENT '激活时间/签约时间',
    area VARCHAR(50) COMMENT '区域代码',
    city_area VARCHAR(100) COMMENT '城市区域',
    communication_address VARCHAR(255) COMMENT '通讯地址',
    level VARCHAR(10) COMMENT '渠道等级',
    registe_capital VARCHAR(50) COMMENT '注册资金',
    firm_scale VARCHAR(50) COMMENT '公司规模',
    comment TEXT COMMENT '备注',
    open_bank VARCHAR(100) COMMENT '开户银行',
    account VARCHAR(100) COMMENT '户名',
    idcard VARCHAR(50) COMMENT '证件号码',
    phone VARCHAR(20) COMMENT '联系电话',
    bank_account VARCHAR(50) COMMENT '银行账号',
    owner VARCHAR(50) COMMENT '渠道经理ID',
    owner_name VARCHAR(50) COMMENT '渠道经理姓名',
    pay_open_bank VARCHAR(100) COMMENT '付款开户行',
    pay_account VARCHAR(100) COMMENT '付款户名',
    pay_bank_account VARCHAR(50) COMMENT '付款卡号',
    parent_channel_id VARCHAR(50) COMMENT '父级渠道编号',
    channel_level VARCHAR(10) DEFAULT '1' COMMENT '渠道等级 1-一级渠道 2-二级渠道',
    exhibition VARCHAR(100) COMMENT '展业模式',
    exhibition_remark VARCHAR(255) COMMENT '展业模式说明',
    major_business VARCHAR(100) COMMENT '主营业务',
    major_business_remark VARCHAR(255) COMMENT '主营业务说明',
    major_business_city VARCHAR(100) COMMENT '业务覆盖城市',
    major_business_city_code VARCHAR(50) COMMENT '业务覆盖城市代码',
    detail_addr VARCHAR(255) COMMENT '详细地址',
    collect_and_return VARCHAR(10) DEFAULT '0' COMMENT '代收代返 0-支持 1-拒绝',
    channel_attribute VARCHAR(10) DEFAULT '2' COMMENT '渠道属性 1-个人 2-机构',
    capital_allocation VARCHAR(10) DEFAULT '0' COMMENT '配资标识 1-是 0-否',
    seal_id VARCHAR(50) COMMENT '印章ID',
    seal_photo VARCHAR(255) COMMENT '印章图片',
    is_hunter VARCHAR(10) DEFAULT '0' COMMENT '是否大B 0-普通渠道 1-大B',
    operater VARCHAR(50) COMMENT '操作人',
    operater_name VARCHAR(50) COMMENT '操作人姓名',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);