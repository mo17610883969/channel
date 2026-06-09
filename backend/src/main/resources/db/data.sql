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
(4, 0, '渠道管理', 'CATALOG', '/channel', 'Layout', 'Connection', 2, NULL, 1),
(5, 4, '渠道列表', 'MENU', '/channel/list', 'channel/list/index', 'List', 1, 'channel:list', 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 2);

INSERT INTO sys_user_menu (user_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);