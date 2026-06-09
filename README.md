# 渠道保证金管理系统 (Channel Margin Management System)

基于 Spring Boot + Vue 3 的渠道保证金管理系统，支持用户管理、菜单管理和基于 RBAC 的权限控制。

## 技术栈

### 后端
| 技术 | 版本 |
|------|------|
| Spring Boot | 3.2.5 |
| Java | 17 |
| MyBatis-Plus | 3.5.6 |
| H2 Database | (内存数据库) |
| JJWT | 0.12.5 |
| Caffeine | 3.1.8 |
| Lombok | — |

### 前端
| 技术 | 版本 |
|------|------|
| Vue | 3.x |
| Vite | 5.x |
| Vue Router | 4.x |
| Pinia | 2.x |
| Element Plus | 2.x |
| Axios | 1.x |

## 项目结构

```
channel-margin/
├── backend/                    # 后端 Spring Boot 项目
│   └── src/main/
│       ├── java/com/channelmargin/
│       │   ├── config/           # 配置类（CORS、MyBatis-Plus、缓存）
│       │   ├── controller/       # 控制器（Auth、User、Menu）
│       │   ├── dto/              # 数据传输对象
│       │   ├── entity/           # 实体类（User、Role、Menu）
│       │   ├── interceptor/      # JWT 拦截器
│       │   ├── mapper/           # MyBatis Mapper 接口
│       │   ├── service/          # 业务逻辑层
│       │   └── util/             # 工具类（JWT）
│       └── resources/
│           ├── db/               # 数据库初始化脚本
│           ├── mapper/           # MyBatis XML 映射文件
│           └── application.yml   # 应用配置
├── frontend/                   # 前端 Vue 3 项目
│   └── src/
│       ├── api/                  # API 请求封装
│       ├── router/               # 路由配置
│       ├── store/                # Pinia 状态管理
│       └── views/                # 页面组件
│           ├── Login.vue         # 登录页
│           ├── Layout.vue        # 布局页
│           └── system/           # 系统管理
│               ├── user/         # 用户管理
│               └── menu/         # 菜单管理
└── README.md
```

## 快速开始

### 环境要求

- **JDK** 17+
- **Maven** 3.6+
- **Node.js** 18+
- **npm** 9+

### 1. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动后运行在 `http://localhost:8080`。

- H2 控制台：`http://localhost:8080/h2-console`
- JDBC URL：`jdbc:h2:mem:channelmargin`，用户名 `sa`，密码留空

### 2. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端启动后运行在 `http://localhost:3000`，API 请求通过 Vite 代理转发到后端。

### 3. 登录系统

打开浏览器访问 `http://localhost:3000`，使用以下账号登录：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `admin` | `admin123` | 超级管理员 |
| `zhangsan` | `admin123` | 普通用户 |
| `lisi` | `admin123` | 普通用户 |

## 功能模块

### 用户管理
- 用户列表（分页、搜索）
- 新增 / 编辑 / 删除用户
- 用户状态启禁
- 角色分配

### 菜单管理
- 树形菜单展示
- 菜单新增 / 编辑 / 删除
- 支持目录、菜单两种类型
- 权限标识配置

### 认证与授权
- JWT Token 认证（有效期 2 小时）
- 密码 MD5 加密存储
- 路由守卫（未登录自动跳转登录页）
- 基于角色的菜单权限控制

## API 接口

### 认证

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| GET | `/api/auth/userinfo` | 获取当前用户信息 |
| GET | `/api/auth/menus` | 获取用户菜单 |
| POST | `/api/auth/logout` | 退出登录 |

### 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 分页查询用户列表 |
| GET | `/api/users/{id}` | 获取用户详情 |
| POST | `/api/users` | 新增用户 |
| PUT | `/api/users` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| PUT | `/api/users/{id}/status` | 更新用户状态 |

### 菜单管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/menus` | 获取菜单树 |
| GET | `/api/menus/{id}` | 获取菜单详情 |
| POST | `/api/menus` | 新增菜单 |
| PUT | `/api/menus` | 更新菜单 |
| DELETE | `/api/menus/{id}` | 删除菜单 |

## 配置说明

### 后端配置 (`application.yml`)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:channelmargin;MODE=MySQL;DB_CLOSE_DELAY=-1

jwt:
  secret: channelmargin-jwt-secret-key-2026-channel-margin-management-system
  expiration: 7200000  # 2小时
```

### 前端代理配置 (`vite.config.js`)

```js
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 数据库

项目使用 H2 内存数据库，启动时自动执行 `schema.sql` 和 `data.sql` 初始化表结构和测试数据。如需持久化，将 JDBC URL 改为文件模式：

```
jdbc:h2:file:./data/channelmargin;MODE=MySQL
```