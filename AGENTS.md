# PeterAlbus Blog 后端协作指南

本仓库是 PeterAlbus Blog 的 Spring Boot 后端。修改代码前，按任务读取：

- 涉及模块、分层、数据、鉴权、Redis、文件或运行环境时，阅读 [`docs/architecture.md`](docs/architecture.md)。
- 涉及 controller、参数、响应、领域字段或前端联调时，阅读 [`docs/api-contract.md`](docs/api-contract.md)，并同时核对前端仓库的 `src/services/`、直接上传页面和 `docs/backend-contract.md`。`hikari` 分支特有的消息、背景图和照片删除能力也记录在该接口文档中。
- `pom.xml`、controller、domain 和 `applicationExample.yml` 分别是依赖、HTTP 行为、数据字段和 Sa-Token 样例配置的权威来源；文档用于建立导航和稳定契约。

## 核心边界

- 请求链路保持 `controller → service → service/impl → mapper → MySQL`。controller 负责 HTTP 绑定、权限与响应装配；service 负责领域处理；mapper 只承接 MyBatis-Plus 持久化。
- 当前没有 DTO 层：`domain` 对象同时用于表单绑定、数据库映射和 JSON 序列化。修改字段会同时影响数据库列、controller 参数和前端响应，必须全链路核对。
- 除 multipart 外，请求主要绑定 query 或 `application/x-www-form-urlencoded` 字段，不接收 JSON body。不要只改注解或只改前端编码。
- 所有业务响应使用 `Result<T> { code, message, data }`。业务错误由响应体 code 表达，通常不会改变 HTTP 状态；前端依赖这一行为。
- Snowflake `Long` ID 通过 `@JsonFormat(shape = STRING)` 输出，防止 JavaScript 精度丢失。新增或修改 ID 字段时保持字符串序列化契约。
- Sa-Token header/cookie 名固定为 `satoken_peteralbus_blog`。权限由 `StpInterfaceImpl` 根据 `userIdentity` 映射；UI 条件不能替代 controller 校验。
- Redis 同时承载 Sa-Token、验证码和文章访问去重。修改 key、TTL 或序列化配置时必须检查所有生产者和消费者。
- 图片写入宿主机绝对目录并返回 `file.peteralbus.com` URL。照片删除和背景图增删复用同一文件边界；修改这些逻辑前先阅读架构文档中的文件系统约束。

## 修改要求

- 接口变更必须选择一个明确的新契约，并同时修改 controller、service/domain、前端调用与两端接口文档；不要增加旧字段、旧路径或旧响应的兼容分支。
- 新增领域能力时沿用现有最短分层，不为简单 CRUD 增加额外抽象。若业务规则属于 service，不要藏在 mapper 或工具类中。
- 权限变更要同时检查 `PermissionConfig`、`StpInterfaceImpl`、所有调用 `StpUtil` 的 controller 方法和前端入口显示。
- 不要在日志、文档、测试或提交内容中复制配置凭证。`application.yml` 与真实 `SmsUtil.java` 已被忽略；跟踪的 `applicationExample.yml` 只能保留不含秘密的 Sa-Token 配置，不能再次加入数据源、Redis、邮件或其他真实连接信息。
- 当前仓库没有数据库 migration/schema。字段或约束变化若没有对应数据库变更说明，就不能视为完整实现。
- 不要顺手修复 `docs/api-contract.md` 中列出的已知偏差。需要修复时，把对应前后端调用链作为一个明确任务一次改完。

## 本地准备与验证

需要 Java 17、MySQL、Redis、邮件配置；完整运行配置应放在被忽略的 `src/main/resources/application.yml` 中，不要写回样例文件。短信能力还需要从 `src/main/java/com/peteralbus/util/SmsUtil.java.example` 创建被忽略的 `SmsUtil.java` 并填入本地凭证。全新检出缺少该文件时不能完成编译。

本地开发环境使用 Java 17 和 Maven。优先调用已安装的 `mvn`；仓库中的 `mvnw` 未设置可执行位，仅在需要 wrapper 时通过 shell 调用：

```sh
mvn -DskipTests package
mvn test
# wrapper 等价调用：sh ./mvnw test
```

唯一现有测试是 Spring context smoke test，会加载完整应用上下文，可能依赖本地配置和外部服务。按改动风险补充 service/controller 测试；接口修改至少验证成功、业务失败、未登录、无权限和字段绑定路径。

提交前确认：`git diff --check`、编译/测试结果、配置中无新增秘密、数据库变更已说明、以及前后端契约文档已同步。若运行环境缺 Java、服务或凭证，应报告具体未验证项，不要声称验证通过。
