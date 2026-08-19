# HTTP 接口契约

本文描述 `hikari` 分支当前 controller 的真实行为。它是维护地图，不替代源码；controller、domain 或权限映射改变时必须同步更新。前端实际消费方式见相邻前端仓库的 `docs/backend-contract.md`。

## 全局约定

### 基地址与跨域

现有前端和部署约定以端口 `8089` 访问服务；跟踪的配置样例只保留 Sa-Token 配置，并不声明端口，本地或部署配置必须自行设置。所有领域 controller 都标注了 `@CrossOrigin`。路径以下表中的 controller 前缀为根。

### 请求绑定

- 没有 controller 使用 `@RequestBody`；普通参数由 query string 或表单字段绑定。
- POST 调用通常应使用 `application/x-www-form-urlencoded`。
- 文件上传使用 `multipart/form-data`，文件字段名固定为 `file`。
- 标为 `ANY` 的接口使用未限定 method 的 `@RequestMapping`，Spring 当前接受多种 HTTP 方法；表中仍说明现有前端约定。
- 参数没有 Bean Validation 注解。缺字段时可能绑定为 null，并在后续逻辑中失败或抛异常。

### 响应

所有正常 controller 返回：

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

`code` 是业务码：成功为 `200`，已知业务失败使用 `400`、`403` 或 `500`。`ResultUtil` 不设置 HTTP status，因此这些业务失败通常仍以 HTTP 200 传输。未捕获异常和 Spring 参数绑定错误不保证遵守该响应包。

### ID 与时间

所有实体主键和评论关联 ID 是 Java `Long`，JSON 序列化为字符串。消费者不得把它们转成 JavaScript number。

`LocalDateTime` 输出格式为 `yyyy-MM-dd HH:mm:ss`、时区 GMT+8；`Blog.blogTime` 是日期字段。

### Token 与权限

token 名称为 `satoken_peteralbus_blog`，可由同名 header/cookie 传递。权限映射：

- `userIdentity <= 0`：发布、修改文章；
- `userIdentity <= 1`：删除任意评论；
- `userIdentity <= 1`：删除照片；
- `userIdentity <= 5`：发表评论；
- 评论作者可以删除自己的评论；
- 用户资料修改要求 token 中 loginId 与目标 userId 相同。

未列出权限检查的写接口当前没有 controller 级访问控制。

## 数据模型

### `Blog`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `blogId` | string ID | 主键 |
| `blogTitle` | string | 标题 |
| `blogImg` | string | 封面 URL |
| `blogType` | integer | 前端分类值 |
| `blogDescription` | string | 摘要 |
| `blogAuthor` | string | 作者显示名 |
| `blogContent` | string | Markdown 正文；列表接口返回空字符串 |
| `blogTime` | date | 发布日期，新增时由服务设置 |
| `blogLike` | integer | 点赞数 |
| `blogViews` | integer | 浏览数 |
| `isTop` | integer | `1` 表示置顶 |
| `blogHide` | boolean | `true` 时从列表排除 |
| `gmtModified` | datetime | 新增/更新时由服务设置 |

### `User`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `userId` | string ID | 主键 |
| `userUsername` | string | 显示名 |
| `userPassword` | string | 入参为明文；持久化为 salted MD5；响应应脱敏 |
| `userPhone` | string | 手机账号 |
| `userMail` | string | 邮箱账号 |
| `userIdentity` | integer | `0` 站长、`1` 管理员、`5` 普通用户 |
| `userAvatar` | string | 头像 URL |
| `userSalt` | string | 密码 salt；响应应脱敏 |
| `gmtCreate` | datetime | 注册时由服务设置 |

### `Comment`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `commentId` | string ID | 主键 |
| `commentTarget` | integer | `1` 文章，`2` 一级评论 |
| `commentTargetId` | string ID | 目标文章或一级评论 ID |
| `commentUserId` | string ID | 作者 ID；`-1` 表示匿名展示 |
| `commentContent` | string | 评论正文 |
| `gmtCreate` | datetime | 新增时由 controller 设置 |
| `gmtModified` | datetime | 新增/更新时由 controller 设置 |

### 其他模型

- `FriendLink`: `linkId`(string ID), `linkName`, `linkUrl`。
- `Music`: `musicId`(string ID), `name`, `artist`, `url`, `cover`, `lrc`；字段形状直接供 APlayer 使用。
- `Photo`: `imgId`(string ID), `imgName`, `imgSrc`, `imgThumb`。
- `BackgroundImage`: `backgroundId`(string ID), `backgroundUrl`, `backgroundPath`, `backgroundDescription`, `isShow`。
- `Message`: `messageId`(string ID), `senderId`(string ID), `targetId`(string ID), `messageContent`, `messageTitle`, `senderName`, `isRead`, `gmtCreate`。

## User API

前缀：`/user`。

| 方法 | 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- | --- |
| ANY（前端 GET） | `/mailLogin` | `userMail`, `userPassword` | 无 | Sa-Token `SaTokenInfo` |
| ANY（前端 GET） | `/phoneLogin` | `userPhone`, `userPassword` | 无 | `SaTokenInfo` |
| ANY（前端 GET） | `/isLogin` | token | 有效登录 | 当前 `User`，password/salt 为 null |
| ANY（前端 GET） | `/logout` | token | 当前会话 | null |
| ANY（前端 GET） | `/applyMailVerifyCode` | `account` | 无 | null |
| ANY（前端 GET） | `/applyPhoneVerifyCode` | `account` | 无 | null |
| ANY | `/applyResetPasswordVerifyCode` | `account`, `type`；`type === "mail"` 走邮件，否则走短信 | 无 | null |
| ANY（前端 POST form） | `/register` | User 注册字段, `verifyCode` | 对应注册验证码 | 注册服务返回的字符串 |
| ANY（前端 GET） | `/changePassword` | `userId`, `oldPassword`, `newPassword` | 本人 | null；并注销该 userId |
| ANY（前端 GET） | `/setPhone` | `userID`, `userPhone`, `verifyCode` | 本人及新手机号验证码 | null |
| ANY（前端 GET） | `/setMail` | `userID`, `userMail`, `verifyCode` | 本人及新邮箱验证码 | null |
| ANY | `/resetPassword` | `account`, `verifyCode`, `newPassword` | 重置验证码 | null |
| ANY multipart | `/uploadAvatar` | `file`, `userId` | 本人 | 头像 URL string |
| ANY（前端 GET） | `/changeUsername` | `userId`, `username` | 本人 | null |
| ANY（前端 GET） | `/getUserById` | `userId` | 无 | 公开 `User`，phone/mail/password/salt 为 null |

登录失败、账号已注册、验证码错误等通常返回业务 `400`。资料越权返回业务 `403`。发送、上传或数据库失败通常返回业务 `500`。

注册的实现会将 `UserService.register` 返回字符串直接包装为成功响应；该字符串可能是 userId，也可能是 `repeatAccount`、异常消息或 null。因此 `code === 200` 并不保证数据库插入成功，这是当前实现行为。

## Blog API

前缀：`/blog`。

| 方法 | 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- | --- |
| GET | `/queryAll` | 无 | 无 | 非隐藏 `Blog[]`，每项 `blogContent = ""`；无显式排序 |
| GET | `/queryById` | `id` | 无 | 完整 `Blog`；不检查 `blogHide` |
| POST form | `/add` | 可绑定的 Blog 字段 | `write-article`，即 identity <= 0 | 新增并补充时间后的 Blog |
| POST form | `/update` | Blog 字段，至少需 `blogId` | `modify-article`，即 identity <= 0 | 更新后的 Blog |
| GET | `/visitBlog` | `blogId`, `ipAddress` | 无 | 首次为 `"success"`；8 小时重复为 null |
| POST multipart | `/upload` | `file` | 无显式检查 | 封面 URL string |

访问计数使用 Redis key `blogVisitRecord:<blogId>-<ipAddress>` 去重 8 小时。IP 完全由调用方提交；首次访问会读取文章、增加 `blogViews`、调用更新服务并刷新 `gmtModified`。

## Comment API

前缀：`/comment`。所有方法均为 ANY；当前前端读取用 GET，写入用 POST form。

| 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- |
| `/getCommentByBlogId` | `blogId` | 无 | `commentTarget = 1` 且目标为该 blogId 的 Comment[] |
| `/getCommentByCommentId` | `commentId` | 无 | `commentTarget = 2` 且目标为该 commentId 的 Comment[] |
| `/getCommentByUserId` | `userId` | 无 | 见已知偏差 |
| `/getCommentById` | `commentId` | 无 | Comment 或 null |
| `/addComment` | Comment 字段 | `comment` 权限 | 新增并补充时间后的 Comment |
| `/updateComment` | Comment 字段 | loginId 等于提交的 `commentUserId` | 更新后的 Comment |
| `/deleteComment` | 至少 `commentId`, `commentUserId` | 本人或 `delete-comment` 权限 | null |

新增评论时，只要提交的 `commentUserId != -1`，controller 就会把它替换为 token 中的 loginId；提交 `-1` 时保留匿名 ID。两种情况都必须先通过评论权限检查。数据库写入成功后，service 会同步创建评论或回复提醒消息；消息创建失败不会回滚评论。

## FriendLink API

前缀：`/friendLink`，均为 ANY。

| 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- |
| `/getFriendLinkList` | 无 | 无 | `FriendLink[]`，无显式排序 |
| `/addFriendLink` | `linkName`, `linkUrl` 等可绑定字段 | 无显式检查 | 新增后的 FriendLink |

## Music API

前缀：`/music`，均为 ANY。

| 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- |
| `/queryAll` | 无 | 无 | `Music[]`，无显式排序 |
| `/add` | Music 字段 | 无显式检查 | null |
| `/delete` | `musicId` | 无显式检查 | null |

## Photo API

前缀：`/photo`。

| 方法 | 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- | --- |
| GET | `/queryAll` | 无 | 无 | `Photo[]`，无显式排序 |
| ANY multipart | `/upload` | `file`, `imgName` | 无显式检查 | `{ url, thumbnailUrl }` |
| POST multipart | `/uploadOriginImg` | `file`, `path` | 无显式检查 | 原图 URL string |
| GET | `/deletePhotoByUrl` | `photoUrl` | `delete-image`，即 identity <= 1 | null；message 描述删除结果 |
| GET | `/deletePhotoById` | `photoId` | `delete-image`，即 identity <= 1 | null；message 描述数据库和文件删除结果 |

`/upload` 把文件保存到固定照片目录，生成 200×300 范围的缩略图，并新增数据库记录。`/uploadOriginImg` 直接使用调用方提供的 `path` 拼接服务端根目录；这是当前接口边界的一部分。`deletePhotoByUrl` 只接受以公开文件基地址开头的 URL，并同时尝试删除对应缩略图；`deletePhotoById` 先删除数据库记录，再删除原文件，因此文件删除失败时不会恢复数据库记录。

## BackgroundImage API

前缀：`/background`。

| 方法 | 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- | --- |
| ANY | `/queryAll` | 无 | 无 | `BackgroundImage[]`，无显式排序 |
| ANY | `/queryById` | `backgroundId` | 无 | `BackgroundImage` 或 null |
| POST multipart | `/add` | `file`, `description` | 无显式检查 | null |
| POST form/query | `/update` | `backgroundId`, `description` | 无显式检查 | null |
| POST form/query | `/delete` | `backgroundId` | 无显式检查 | null；当前实现通常返回业务 400，见已知偏差 |

`add` 限制文件不超过 512 MiB，把文件写入 `blog/static/background`，但当前没有把 `description` 写入新实体。`update` 只更新描述。`delete` 先尝试删除文件，文件步骤成功后才删除数据库记录。

## Message API

前缀：`/message`，均为 ANY。

| 路径 | 输入 | 鉴权 | 成功 `data` |
| --- | --- | --- | --- |
| `/getInBoxMessage` | 无 | 已登录 | 当前用户作为 `targetId` 的 `Message[]` |
| `/getOutBoxMessage` | 无 | 已登录 | 当前用户作为 `senderId` 的 `Message[]` |
| `/getUnreadMessageCount` | 无 | 登录时按当前用户查询；未登录也成功 | 未读数量；未登录时为 `0` 且 message 为 `未登录` |
| `/readMessage` | `messageId` | service 要求当前用户是消息接收者 | null |

消息列表没有显式排序。`readMessage` 的 controller 没有先检查登录；service 读取当前 loginId 并拒绝修改他人的消息。未登录、消息不存在等路径可能抛出未包装异常。

## 已知契约偏差与风险点

这些是当前实现事实，应在明确修复任务中一次性改正，而不是新增兼容分支：

1. `setPhone`/`setMail` 的 Java 参数名是 `userID`，当前前端发送 `userId`。
2. `getCommentByUserId` 的 service 实际条件是 `comment_target_id = userId`，不是 `comment_user_id = userId`。
3. 多个有副作用的接口使用 ANY 或 GET，业务错误又通常通过 HTTP 200 返回。
4. 友链新增、音乐增删、背景图写接口和三类上传没有 controller 权限检查；`uploadOriginImg` 还接受调用方路径。
5. 登录 service 在查询结果为 null 时会直接访问用户字段；部分需要登录的方法在无 token 时直接调用 `getLoginId`。这些路径可能抛出未包装异常。
6. `queryById` 可读取隐藏文章；访问计数依赖客户端 IP 且假定文章和 `blogViews` 非 null。
7. 文件落盘、数据库写入和删除之间没有事务；文件名只通过扩展名白名单判断图片类型。按照片 ID 删除时数据库先于文件，失败会留下不一致状态。
8. 背景图删除把宿主机路径传给只接受公开 URL 的删除方法，因此文件校验失败，数据库记录也不会删除。
9. 匿名评论仍进入消息装配流程，消息工具会按 `-1` 查询发送者并访问用户字段，可能抛出未包装异常；消息写入与评论写入也不在同一事务中。

修复时应先定义唯一目标行为，再同步 controller、service、前端调用、测试和两端文档。
