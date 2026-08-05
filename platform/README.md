# Migration Platform (Java 8)

基于《迁移平台软件设计说明书》落地的 **分布式 / 低耦合 / 组件可拔插** Java 8 基础框架。

## 模块

| 模块 | 职责 |
|------|------|
| `platform-spi` | Reader/Writer/LogAdapter/TypeMapper SPI；Permission/Audit/Crypto 安全 SPI；Record 模型 |
| `platform-core` | Channel、插件容器（ClassLoader 隔离 + plugin.json）、统计、安全占位实现 |
| `platform-component-api` | DBCat/FullImport/Store/IncrSync/Verifier/AIEngine/Supervisor 组件契约 |
| `platform-control` | 控制面：编排、元数据、节点注册、作业下发、Supervisor |
| `platform-runtime` | 数据面：SyncWorker（Reader→Channel→Writer）、StreamRuntime 骨架 |
| `platform-plugins-demo` | 内存 Reader/Writer 示例插件 |
| `platform-bootstrap` | 本地端到端 demo 入口 |

依赖方向：`bootstrap → control/runtime/plugins → component-api/core → spi`（单向，无实现互引）。

## 环境

- JDK 8+（源码与字节码目标为 **1.8**）
- Maven 3.6+

## 构建与运行

```bash
cd platform
mvn -q clean test package
mvn -pl platform-bootstrap -am exec:java -Dexec.mainClass=com.migration.platform.bootstrap.DemoBootstrap
```

Demo 流程：注册 demo 插件 → 权限/审计校验 → 控制面下发作业 → SyncWorker 跑通 50 行内存全量管道 → 打印吞吐统计。

## 新增插件

1. 实现 `ReaderPlugin` / `WriterPlugin`（或 LogAdapter / TypeMapper）
2. 提供 `plugin.json`：

```json
{
  "name": "my-reader",
  "type": "READER",
  "class": "com.example.MyReader",
  "version": "0.1.0"
}
```

3. 放入插件目录（含 jar 或 `classes/`），调用 `PluginContainer.loadDirectory(dir)`；本地也可 `registerInProcess(...)`。

## 设计对齐

- 控制面 / 数据面分离（Orchestrator + JobDispatcher ↔ SyncWorker / StreamRuntime）
- 分布式基础：`InMemoryNodeRegistry` 心跳与作业路由（可替换 ZK/etcd）
- 安全横切 SPI：`PermissionService` / `AuditService` / `CryptoService`
- 全量管道严格 Writer 先启动，Channel 背压

> 本骨架不含真实 JDBC/CDC 生产实现；安全加解密为 Demo 占位，不可用于生产。
