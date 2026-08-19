# PeterAlbusBlogHikari

## 仓库文档

- [智能体协作指南](AGENTS.md)
- [后端代码结构与运行链路](docs/architecture.md)
- [HTTP 接口契约](docs/api-contract.md)

基于Springboot的博客后端，

完全对于后端进行了重构，重写了返回值结构。

更新了Spring与Java版本，与前端进行了统一对接。

## 本地开发环境

项目使用 Java 17 和 Maven。完整运行配置放在被 Git 忽略的 `src/main/resources/application.yml`，不要把数据库、Redis、邮件或短信凭据写入 `applicationExample.yml`。

```sh
java -version
mvn -version
mvn -DskipTests package
```

本地短信实现文件 `src/main/java/com/peteralbus/util/SmsUtil.java` 同样被 Git 忽略；首次检出时需根据 `.example` 文件配置，否则源码无法完成编译。
