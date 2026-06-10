-- password: admin123 (MD5: 0192023a7bbd73250516f069df18b500)
INSERT INTO sys_user (username, password, nickname, email, phone, status) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '超级管理员', 'admin@channel.com', '13800000000', 1),
('zhangsan', '0192023a7bbd73250516f069df18b500', '张三', 'zhangsan@channel.com', '13800000001', 1),
('lisi', '0192023a7bbd73250516f069df18b500', '李四', 'lisi@channel.com', '13800000002', 1);

INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'ROLE_ADMIN', '拥有所有权限', 1),
('普通用户', 'ROLE_USER', '普通用户权限', 1);

INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, status) VALUES
(1, 0, '系统管理', 'CATALOG', '/system', 'Layout', 'Setting', 1, NULL, 1),
(2, 1, '用户管理', 'MENU', '/system/user', 'system/user/index', 'User', 1, 'sys:user:list', 1),
(3, 1, '菜单管理', 'MENU', '/system/menu', 'system/menu/index', 'Menu', 2, 'sys:menu:list', 1),
(4, 0, '渠道信息管理', 'CATALOG', '/channel', 'Layout', 'Connection', 2, NULL, 1),
(5, 4, '渠道信息列表', 'MENU', '/channel/list', 'channel/channelList', 'List', 1, 'channel:list', 1),
(6, 0, '保证金管理', 'CATALOG', '/margin', 'Layout', 'Wallet', 3, NULL, 1),
(7, 6, '保证金账户列表', 'MENU', '/margin/list', 'marginAccount/index', 'Money', 1, 'margin:list', 1);

-- 添加渠道示例数据
INSERT INTO channel_org (code, name, full_name, type, category, able, recycle, area, city_area, phone, idcard, open_bank, account, bank_account, channel_attribute, capital_allocation, create_time, owner, owner_name, operater, operater_name) VALUES
('JDPTEST001', '金杜测试', '深圳市金杜测试顾问有限公司', '0', '0', '0', '0', '440000', '深圳市', '13800138001', '440305199001011234', '中国工商银行深圳分行', '深圳市金杜测试顾问有限公司', '6222024000123456789', '2', '0', CURRENT_TIMESTAMP, 'admin', '超级管理员', 'admin', '超级管理员'),
('JDPTEST002', '张三渠道', '张三个人渠道', '0', '0', '0', '0', '440000', '广州市', '13800138002', '440305199002021234', '中国建设银行广州分行', '张三', '6222024000123456790', '1', '1', CURRENT_TIMESTAMP, 'admin', '超级管理员', 'admin', '超级管理员');

-- 添加保证金账户示例数据
INSERT INTO margin_account (master_channel_id, master_channel_name, account_name, bank_account, margin_first_balance, recharge_balance, margin_remain_balance, margin_transit_use_balance, margin_refunded_balance, status, remark, creater_id) VALUES
('CH001', '金杜渠道集团', '深圳市金杜顾问有限公司', '6222024000123456789', 50000000, 2000000, 10000000, 5000000, 0, '1', '金杜主渠道保证金账户', '1'),
('CH002', '华南渠道联盟', '广州华南科技有限公司', '6222024000123456790', 30000000, 1000000, 5000000, 2000000, 0, '1', '华南区域渠道保证金账户', '1');

-- 添加保证金账户关联渠道
INSERT INTO margin_account_channel (margin_account_id, channel_id, code, name, city_area) VALUES
(1, 1, 'JDPTEST001', '金杜测试', '深圳市'),
(2, 2, 'JDPTEST002', '张三渠道', '广州市');

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 2);

INSERT INTO sys_user_menu (user_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7);