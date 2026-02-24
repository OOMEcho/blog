<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-5.7+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE.txt)

## 🍟 如果这个项目对你有帮助，请 Star 支持

</div>

## 🌟 项目介绍

Blog 是一个面向个人或团队博客的后端服务，基于 Spring Boot + Spring Security + MyBatis-Plus 构建，提供博客前台接口与管理后台接口，涵盖文章、分类、标签、评论、友链、通知、系统权限、日志审计等完整能力，可作为自建博客或内容管理平台的服务端基座。

### ✨ 功能亮点

- 📰 **博客前台接口**：已发布文章分页/详情、归档、全文搜索、分类与标签、友情链接、站点配置、树形评论及新增评论。
- 🖥️ **博客管理后台**：文章生命周期管理（草稿/发布/审核）、分类标签维护、评论审核、友链审核、博客配置维护、通知公告与定时任务。
- 🔐 **系统与权限管理**：用户/角色/权限/菜单/资源/字典/白名单/文件/日志/通知等模块齐全，支持权限编码驱动菜单和接口。
- 🧱 **平台能力**：JWT 无状态认证、滑块验证码、RSA 密码加密、防重复提交、接口限流、数据脱敏、Excel 导入导出、任务调度、IP 地理识别、操作/登录日志。
- 🗂️ **多存储文件上传**：可按配置切换 LOCAL、MinIO、阿里云 OSS、腾讯云 COS，支持临时链接、批量上传、下载。
- 📚 **开发者友好**：Knife4j API 文档、统一响应格式、MapStruct DTO/VO 映射、HikariCP 性能优化、可插拔配置。

## 🏗️ 技术栈

| 组件 | 版本/说明 |
| --- | --- |
| Spring Boot | 2.7.18 |
| Spring Security | 5.7.x |
| MyBatis-Plus | 3.5.12 |
| Redis / Lettuce | 缓存、限流、会话控制 |
| MySQL + HikariCP | 主数据存储、连接池 |
| MapStruct | DTO/VO 映射 |
| JWT (jjwt 0.13.0) | 认证、Token 刷新 |
| Knife4j 4.5.0 | OpenAPI 文档 |
| Hutool、Jsoup | 工具库/XSS 清理 |
| FastExcel | Excel 导入导出 |
| MinIO / OSS / COS SDK | 文件对象存储 |
| Freemarker | 邮件模版/通知 |

## 📁 目录结构

```
src/main/java/com/blog
├── BlogApplication.java         # 启动类
├── common/                      # 通用组件（结果封装、异常、注解等）
├── config/                      # 安全、MyBatis-Plus、Knife4j 等配置
├── modules/
│   ├── article/                 # 文章管理
│   ├── blog/                    # 前台接口（文章、分类、标签、友链等）
│   ├── blogconfig/              # 博客配置
│   ├── category/ tag/ comment/  # 分类、标签、评论
│   ├── link/                    # 友情链接
│   ├── notification/            # 通知公告/定时任务
│   ├── permission/role/user/... # 系统管理模块
│   └── whitelist/ file/ log/... # 其他支撑模块
└── utils/                       # 工具类

src/main/resources
├── application.yml / application-dev.yml
├── script/blog.sql / blog-data.sql       # 表结构与初始化数据
├── templates/                            # 邮件模板
├── static/                               # 静态资源
└── logback-spring.xml / banner.txt
```

## 🚀 快速开始

### 环境要求

- ☕ JDK 1.8+
- 🗄️ MySQL 5.7+
- 📦 Redis 6.0+
- 🔧 Maven 3.6+（或使用仓库自带 `mvnw`）

### 1. 克隆与编译

```bash
git clone https://github.com/OOMEcho/blog.git
cd blog
# 可选：./mvnw clean compile
```

### 2. 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p blog < src/main/resources/script/blog.sql
mysql -u root -p blog < src/main/resources/script/blog-data.sql
```

默认会创建管理员账号 `admin / 123456` 以及基础权限、白名单、字典数据。

### 3. 修改配置

编辑 `src/main/resources/application.yml`，根据环境调整：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/blog?serverTimezone=Asia/Shanghai&useSSL=false
    username: your_mysql_user
    password: your_mysql_password
  redis:
    host: 127.0.0.1
    port: 6379
    password: your_redis_password
  mail:
    host: smtp.163.com
    username: blog_system@163.com
    password: your_mail_authorization_code

file:
  upload:
    platform: LOCAL   # LOCAL / MINIO / ALIYUN_OSS / TENCENT_COS
jwt:
  secret: change_me_to_a_long_random_string
knife4j:
  basic:
    username: blog
    password: blog
```

### 4. 启动服务

```bash
# 开发环境（默认 8080）
./mvnw spring-boot:run

# 或打包运行
./mvnw clean package -Dmaven.test.skip=true
java -jar target/blog-1.0.0.jar --spring.profiles.active=dev
```

### 5. 访问接口

- 后台 API：`http://localhost:8080`
- Knife4j 文档：`http://localhost:8080/doc.html`（basic auth：`blog/blog`）
- 前台开放接口：`/blog/articles`、`/blog/archives`、`/blog/comments/{id}` 等，可参考 `BlogController`。

## 🐳 Docker 与自动化部署

### 使用 Dockerfile

```bash
# 先打包
./mvnw clean package -Dmaven.test.skip=true

# 构建镜像
docker build -t blog-backend .

# 运行容器（默认暴露 9090，prod 配置）
docker run -d \
  --name blog \
  -p 9090:9090 \
  -v /home/blog/config:/blog/config \
  -v /home/blog/logs:/blog/logs \
  blog-backend
```

### Jenkins 脚本（`build.sh`）

脚本完成以下步骤：Maven 打包 → 停止并删除旧容器 → 删除旧镜像 → 重新构建镜像 → 挂载配置/日志目录并以 `9090` 端口运行。可直接在 CI 中调用。

## ⚙️ 配置说明

`application.yml` 中常见可调项：

- **数据库与连接池**：`spring.datasource.*`，已启用 HikariCP 池化与连接泄漏检测。
- **Redis**：`spring.redis.*`，用于缓存、限流、验证码、Token 黑名单。
- **邮件**：`spring.mail.*`，支持 SSL、超时控制和 Freemarker 模版。
- **文件上传**：`file.upload.platform` (LOCAL/MINIO/ALIYUN_OSS/TENCENT_COS) 以及对应参数。
- **JWT**：`jwt.secret / access-token-expiration / refresh-token-expiration`。
- **安全登录**：`security.login.enable-captcha` 控制滑块验证码，`enable-password-encryption` 控制 RSA 加密。
- **Knife4j**：`knife4j.enable`、`knife4j.basic.*` 控制文档开关与访问凭证。
- **任务调度**：`task.scheduling.enabled` 控制通知等定时任务。
- **CORS**：`cors.allowed-origins` 指定允许的前端域名。
- **日志**：`logging.file.path=./logs`，可挂载至宿主机。

## 📚 API 文档

- 访问地址：`http://localhost:8080/doc.html`
- 认证方式：Basic Auth（默认 `blog/blog`）
- 文档内容：接口分组、参数、响应示例、在线调试、示例代码。

## 🧪 测试

```bash
./mvnw test
./mvnw test -Dtest=UserServiceTest
./mvnw test jacoco:report  # 如需覆盖率
```

## 📦 部署建议

1. `./mvnw clean package -Dmaven.test.skip=true`
2. 创建 `application-prod.yml` 或通过环境变量覆盖敏感配置
3. 关闭生产 Knife4j：`knife4j.enable=false`
4. 设置日志级别：`logging.level.com.blog=INFO`
5. 以 `java -jar target/blog-1.0.0.jar --spring.profiles.active=prod` 或 Docker 方式启动

## 🤝 贡献指南

1. Fork 仓库
2. 创建特性分支 `git checkout -b feature/xxx`
3. 开发 & 添加必要测试
4. `git commit -m "feat: xxx"`
5. `git push` 并提交 Pull Request

欢迎 issue/PR，一起完善博客系统。

## 📄 许可证

本项目基于 [MIT License](LICENSE.txt) 开源，可自由使用、修改与分发，但需保留版权声明。

## ❓ 常见问题

<details>
<summary>如何修改默认管理员账号？</summary>
修改 `t_user` 表的默认记录或通过后台创建新管理员，并更新 `t_user_role` 关联。
</details>

<details>
<summary>无法发送邮件怎么办？</summary>
确认 SMTP 地址、端口、授权码正确，并检查是否启用 SSL (`spring.mail.properties.mail.smtp.ssl.enable=true`)。
</details>

<details>
<summary>如何切换文件存储平台？</summary>
调整 `file.upload.platform`，并完善对应平台的 endpoint/key/bucket 配置，重启服务生效。
</details>

<details>
<summary>如何访问博客前台接口？</summary>
直接调用 `/blog/**` 接口（见 `BlogController`），默认已在白名单(`t_whitelist`)内，无需 Token。
</details>

<details>
<summary>如何启用/关闭限流与验证码？</summary>
`@RateLimiter` 注解用于接口层限流；`security.login.enable-captcha` 控制滑块验证码开关。
</details>

## 📞 支持

- 邮件：228389787@qq.com
- Issues：https://github.com/OOMEcho/blog/issues

---

<div align="center">

Made with ❤️ by 南常

</div>
