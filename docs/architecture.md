# 后端代码结构与运行链路

本文保存维护 `hikari` 分支所需的结构和运行上下文。具体依赖、字段和接口仍以 `pom.xml`、源码与配置为准；模块边界、外部依赖或部署路径变化时，应同步更新本文。

## 系统定位

本仓库是单体 Spring Boot 3.1.2 应用，使用 Java 17，对外提供博客、用户、评论、友链、音乐、照片、背景图和站内消息接口。现有前端与部署约定使用端口 `8089`，但跟踪的样例文件不再声明运行端口；本地 `application.yml` 必须显式配置。各 controller 均开启跨域。

核心依赖：

- Spring Web、Mail、Data Redis、JDBC；
- MyBatis-Plus 3.5.3.1 + MySQL 8 驱动；
- Sa-Token 1.35.0.RC + Redis 持久化；
- Thumbnailator 处理照片缩略图；
- 阿里云短信 SDK；
- Lombok。

## 请求与持久化链路

```text
HTTP form/query/multipart
  → controller（参数绑定、权限、Result 装配）
  → service 接口
  → service/impl（查询条件、时间、密码等规则）
  → mapper（MyBatis-Plus BaseMapper）
  → MySQL
```

该结构没有 DTO、repository 适配层或统一异常处理层。`domain` 类直接承担三种职责：Spring 请求绑定、MyBatis 表映射、Jackson 响应序列化。因此看似局部的字段改动会跨越 HTTP、Java 和数据库边界。

## 包结构

| 路径 | 职责 | 关键事实 |
| --- | --- | --- |
| `PeterAlbusBlogHikariSpringApplication.java` | Spring Boot 入口 | 扫描 `com.peteralbus` |
| `controller/` | 八个领域的 HTTP 接口 | 返回 `Result<T>`，多数表单绑定 |
| `domain/` | 数据库实体与 JSON 模型 | Long ID 输出为字符串 |
| `service/` | 领域服务接口 | 定义 controller 可用能力 |
| `service/impl/` | 查询、写入与领域处理 | 直接调用 MyBatis-Plus mapper |
| `mapper/` | 数据访问 | 全部继承 `BaseMapper<T>`，无 XML 自定义 SQL |
| `config/PermissionConfig.java` | 权限码、角色常量 | 与前端身份显示相关 |
| `config/StpInterfaceImpl.java` | 身份到权限/角色的映射 | 每次按 userId 读取用户 |
| `config/RedisConfig.java` | RedisTemplate 序列化 | key 为字符串，value 为 Jackson JSON |
| `util/ResultUtil.java` | 统一业务响应 | 成功 200；错误使用业务 code |
| `util/RedisUtil.java` | Redis 操作封装 | 验证码、访问记录等共用 |
| `util/TypeUtil.java` | 上传扩展名判断 | 基于文件名后缀 |
| `util/MessageUtil.java` | 站内消息装配 | 评论成功写入后生成提醒消息 |
| `util/FileUtil.java` | 照片目录清理 | 对照数据库记录删除目录中的孤立文件；当前无外部调用入口 |
| `util/SmsUtil.java.example` | 短信实现模板 | 实际 `SmsUtil.java` 被忽略且不随仓库提供 |

## 领域模块

### Blog

`BlogServiceImpl.queryAll` 排除 `blog_hide = true` 的文章，并清空正文以减小列表响应；`queryById` 不做隐藏过滤。新增文章设置发布日期和修改时间，更新文章刷新修改时间。访问计数由 controller 使用客户端提交的 IP 和 Redis 8 小时 key 去重。

### User

注册时设置创建时间、普通用户身份 `5`、8 字符 salt，并用 Sa-Token 的 salted MD5 保存密码。登录按手机或邮箱查询并创建 Sa-Token 会话。验证码由 Redis 保存 10 分钟，邮件经 Spring Mail 发送，短信经本地 `SmsUtil.java` 调用阿里云。

### Comment

`commentTarget` 是多态目标标记：`1` 指向文章，`2` 指向一级评论。一级评论和回复分别查询。非匿名发布时 controller 用登录 ID 覆盖客户端传入的用户 ID；字符串/数值 `-1` 表示匿名显示，但仍要求具备评论权限。

### FriendLink、Music、Photo

友链和音乐是直接的 MyBatis-Plus CRUD。照片上传先把原图写入磁盘，按 200×300 边界生成缩略图，再把 URL 记录写入数据库。原图上传接口可接受调用方提供的相对保存路径。`hikari` 还提供按 URL 删除文件、按照片 ID 删除数据库记录与文件的接口；删除权限只授予 owner 和 admin。

### BackgroundImage、Message

背景图复用 `PhotoService` 写入 `blog/static/background`，数据库同时保存公开 URL 和宿主机路径。查询、上传、描述更新和删除由 `BackgroundImageController` 提供；这些写接口当前没有权限检查。删除实现把宿主机路径传给只接受公开 URL 的照片删除方法，因此当前无法走通，详见接口文档。

评论写入成功后，`CommentServiceImpl` 通过 `MessageUtil` 创建站内消息：文章评论通知固定 owner，回复通知一级评论作者。消息 controller 只暴露收件箱、发件箱、未读数和设为已读，没有直接发送接口。消息正文包含供前端渲染的博客相对链接 HTML。

## 数据与数据库约定

- MyBatis-Plus 开启 `map-underscore-to-camel-case`，Java camelCase 字段映射数据库 snake_case 列。
- `Comment` 显式映射到带反引号的 `comment` 表；其他实体依赖默认表名推导。
- 主键使用 `IdType.ASSIGN_ID`。
- 仓库不含 schema、建表脚本或 migration，因此不能从本仓库独立创建数据库。
- 文章日期使用 `java.sql.Date`；其他创建/修改时间使用 `LocalDateTime`，JSON 时区为 GMT+8，格式为 `yyyy-MM-dd HH:mm:ss`。

## 鉴权与权限

Sa-Token 名称为 `satoken_peteralbus_blog`，token 存储在 Redis。当前配置允许同账号并发并共享 token，且 `active-timeout = -1`。

| `userIdentity` | 角色 | 权限 |
| --- | --- | --- |
| `0` | `owner` | `*`, 用户管理, 发布/修改文章, 删除任意评论, 删除图片, 评论 |
| `1` | `admin` | 删除任意评论, 删除图片, 评论 |
| `5` | `user` | 评论 |

controller 逐方法调用 `StpUtil`，没有全局 URL 拦截规则。没有显式检查的方法就是当前未受保护的方法；完整清单见 [`api-contract.md`](api-contract.md)。

## Redis key 与生命周期

| Key | Value | TTL | 生产/消费位置 |
| --- | --- | --- | --- |
| `verifyCode:<account>` | 注册/绑定验证码 | 10 分钟 | `UserController` |
| `verifyCode_reset:<account>` | 重置密码验证码 | 10 分钟 | `UserController` |
| `blogVisitRecord:<blogId>-<ipAddress>` | `visited` | 8 小时 | `BlogController.visitBlog` |

Sa-Token 自己的 Redis key 由框架管理，不应与业务 key 混用。

## 文件与外部服务

上传不是可移植存储抽象：

- 文章封面写入 `/home/PeterAlbus/assets/blog/imgs/cover/`；
- 用户头像写入 `/home/PeterAlbus/assets/blog/imgs/avatar/`；
- `PhotoServiceImpl` 以 `/home/PeterAlbus/assets/` 为根目录；
- 背景图通过 `PhotoServiceImpl` 写入 `/home/PeterAlbus/assets/blog/static/background/`；
- 返回 URL 均位于 `https://file.peteralbus.com/assets/`。

目标目录必须预先存在并可写。实现不创建目录，文件和数据库写入也没有事务补偿；照片文件成功但数据库插入失败时可能遗留文件。

运行还依赖 MySQL、Redis、SMTP 和阿里云短信。包含完整连接信息的 `application.yml` 被 `.gitignore` 排除；跟踪的 `applicationExample.yml` 仅保留不含秘密的 Sa-Token 配置，不是完整的本地配置模板。数据库、Redis、邮件和端口配置只能写入本地忽略文件或部署环境，不能重新加入该样例。

短信模板与 controller 都以 `smsSendSuccess` 表示发送成功。若修改该标记，必须同时修改生产者和两个验证码调用点。

## 启动与验证限制

- Java 基线是 17。
- 本地包管理与构建使用 Maven；`mvn -DskipTests package` 可完成依赖解析、主源码/测试源码编译和可执行 JAR 打包。
- 全新检出缺少真实 `SmsUtil.java`，需要从 `.example` 创建本地忽略文件后才能编译。
- Maven wrapper 文件当前没有可执行位；优先使用系统 `mvn`，需要 wrapper 时可用 `sh ./mvnw ...`。
- 唯一自动测试是 `@SpringBootTest contextLoads`，会加载完整上下文；它不是独立于外部配置的单元测试。

因此“编译通过”“上下文启动”和“真实接口可用”是三个不同验证层级，应分别陈述。
