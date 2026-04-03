<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2B%20%2F%208.0%2B-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0%2B-red.svg)](https://redis.io/)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.12-2b6cb0.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE.txt)

## 🍟 如果这个项目对你有帮助，请 Star 支持

</div>

## 🌟 项目简介

`blog` 是博客系统的后端服务，基于 Spring Boot + Spring Security + MyBatis-Plus 构建，覆盖博客前台接口和管理后台接口。

项目提供完整的内容管理、权限管理、文件管理、日志审计和通知能力，可直接作为个人博客或轻量 CMS 的后端基座。

## 🔗 相关项目

- 后台管理端（Vue2）：<https://github.com/OOMEcho/blog-admin>
- 博客前台（Vue2）：<https://github.com/OOMEcho/blog-web>
- 当前仓库（后端）：<https://github.com/OOMEcho/blog>

## 🧭 架构设计

### 1) 系统整体架构

```text
┌───────────────────────┐      ┌───────────────────────┐
│ blog-admin (9099)     │      │ blog-web (9088)       │
│ 后台管理端             │      │ 博客前台               │
└──────────┬────────────┘      └──────────┬────────────┘
           │ HTTP /api                     │ HTTP /api
           └──────────────┬────────────────┘
                          ▼
                 ┌──────────────────┐
                 │ blog-api (9090)  │
                 │ Spring Boot      │
                 └───────┬──────────┘
                         ├───────────────► MySQL
                         ├───────────────► Redis
                         └───────────────► 本地/MinIO/OSS/COS
```

### 2) 后端代码架构

| 层级 | 目录 | 职责 |
| --- | --- | --- |
| 接口层 | `modules/**/controller` | 参数校验、接口定义、响应输出 |
| 业务层 | `modules/**/service` | 业务编排、事务控制、领域规则 |
| 持久层 | `modules/**/mapper` | MyBatis-Plus 数据访问 |
| 领域层 | `modules/**/domain` | 实体、DTO、VO、枚举 |
| 通用层 | `common` | 统一响应、异常、切面、限流、脱敏、日志 |
| 配置层 | `config` | Security、Redis、MyBatis-Plus、CORS、调度、序列化 |

### 3) 核心业务模块

| 模块 | 说明 |
| --- | --- |
| `article` | 文章管理与审核流（草稿/发布/待审核/驳回） |
| `blog` | 前台公开接口（文章、归档、搜索、分类、标签、友链、配置） |
| `user/role/menu/permission` | RBAC 体系与菜单权限 |
| `resource/whitelist` | 动态接口鉴权与白名单放行 |
| `file` | 文件上传下载、预签名上传、临时下载链接 |
| `log` | 登录日志、操作日志、导出 |
| `notification` | 站内通知与定时清理 |

### 4) 认证与鉴权链路

1. `POST /login` 支持账号密码/邮箱验证码两种登录方式。
2. 登录成功返回 Access Token，并通过 HttpOnly Cookie 下发 Refresh Token。
3. 访问接口时基于 Token + 资源权限码 + 角色权限进行动态授权。
4. `t_whitelist` 命中白名单直接放行，`t_resource` 维护接口到权限码映射。

## ✨ 核心优势

- 安全完整：滑块验证码、RSA 密码加密、双 Token 刷新、即时失效机制。
- 权限灵活：RBAC + Resource + Whitelist 三层组合，支持动态授权。
- 业务闭环：文章、分类、标签、友链、通知、日志、文件等模块开箱即用。
- 存储可扩展：同一套文件接口可切换 LOCAL / MinIO / OSS / COS。
- 工程化完善：统一返回体、AOP 记录日志、TraceId 链路追踪、可观测日志输出。

## 🏗️ 技术栈

| 组件 | 版本/说明 |
| --- | --- |
| Spring Boot | 2.7.18 |
| Spring Security | 5.7.x（自定义过滤器 + 动态权限） |
| MyBatis-Plus | 3.5.12 |
| MySQL + HikariCP | 主数据存储 + 连接池 |
| Redis / Lettuce | 缓存、验证码、限流、Token 会话 |
| Knife4j | 4.5.0 |
| MapStruct | 1.6.3 |
| FastExcel | 1.3.0 |
| Hutool / Jsoup | 工具库与 XSS 清洗 |
| Freemarker | 邮件模板渲染 |

## 🚀 快速开始

### 1) 环境准备

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+ 或 8.0+
- Redis 6.0+

### 2) 克隆项目

```bash
git clone https://github.com/OOMEcho/blog.git
cd blog
```

### 3) 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p blog < src/main/resources/script/blog.sql
mysql -u root -p blog < src/main/resources/script/blog-data.sql
```

默认初始化数据包含管理员账号：`admin / 123456`（请上线前修改）。

### 4) 修改配置

编辑 `src/main/resources/application.yml`（或新增外部配置覆盖）：

```yaml
server:
  port: 9090

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/blog?serverTimezone=Asia/Shanghai&useSSL=false
    username: your_mysql_user
    password: your_mysql_password
  redis:
    host: 127.0.0.1
    port: 6379
    password: your_redis_password

file:
  upload:
    platform: LOCAL
    public-base-url: http://127.0.0.1:9090

security:
  login:
    enable-captcha: true
    enable-password-encryption: true
    cookie-path: /api/profile/refreshToken
```

说明：

- 管理端/前台走 `/api` 前缀代理时，`cookie-path` 建议为 `/api/profile/refreshToken`。
- 直连后端（无 `/api` 前缀）时，`cookie-path` 建议改为 `/profile/refreshToken`。

### 5) 启动服务

```bash
# 开发启动
mvn spring-boot:run

# 或打包运行
mvn clean package -Dmaven.test.skip=true
java -jar target/blog-1.0.0.jar --spring.profiles.active=dev
```

### 6) 启动后验证

- 服务地址：`http://127.0.0.1:9090`
- API 文档：`http://127.0.0.1:9090/doc.html`
- 文档账号（默认）：`blog / blog`
- 前台公开接口自检：`GET /blog/config`、`GET /blog/articles`

## 🌐 部署说明

### 1) JAR 部署

```bash
mvn clean package -Dmaven.test.skip=true
java -jar target/blog-1.0.0.jar --spring.profiles.active=prod
```

### 2) Docker 部署

```bash
mvn clean package -Dmaven.test.skip=true
docker build -t blog .

docker run -d --restart=always \
  --name blog \
  -p 9090:9090 \
  -v /home/blog/config:/blog/config \
  -v /home/blog/logs:/blog/logs \
  blog
```

如果你使用外部配置，建议在 `/home/blog/config` 放置 `application-prod.yml`。

### 3) 反向代理（可选）

```nginx
server {
  listen 80;
  server_name api.yourblog.cn;

  location / {
    proxy_pass http://127.0.0.1:9090;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
```

## 🧩 三端联调

```bash
# 1) 启动后端
cd D:\Project\OtherProjects\blog
mvn spring-boot:run

# 2) 启动后台管理端
cd D:\Project\FrontEndProjects\blog-admin
npm install
npm run serve

# 3) 启动博客前台
cd D:\Project\FrontEndProjects\blog-web
npm install
npm run serve
```

默认本地端口：

- 后端：`9090`
- 管理端：`9099`
- 前台：`9088`

## 🖼️ 接口预览

- Knife4j 文档：`/doc.html`
- 推荐在部署完成后录入你自己的接口截图或演示链接。

## ❓ 常见问题

1. 登录成功但很快 401：检查前端是否带 `/api` 前缀，并核对 `security.login.cookie-path`。
2. 邮件验证码发送失败：检查 `spring.mail.*` 配置与邮箱授权码。
3. 跨域报错：检查 `cors.allowed-origins` 是否包含前端域名。
4. 文件无法访问：检查 `file.upload.public-base-url` 与存储平台配置是否一致。

## 🤝 贡献

1. Fork 仓库
2. 创建分支：`git checkout -b feature/xxx`
3. 提交代码：`git commit -m "feat: xxx"`
4. 发起 Pull Request

## 📄 许可证

本项目基于 [MIT License](LICENSE.txt) 开源。

---

<div align="center">

Made with ❤️ by OOMEcho

</div>
