# Migration Platform（按设计图对齐的 Java 8 框架）

对应 SDD 图：

- 图 3-1 逻辑架构（源 → 平台 → 目标）
- 图 3-2 三层模块 + 横切安全 + Supervisor
- 图 3-3 控制平面 / 数据平面

## 与设计图映射

| 设计图模块 | 代码位置 |
|------------|----------|
| 服务接入层·传输项目管理 | `platform-access` → `TransferProjectService` |
| 服务接入层·数据源管理 | `platform-access` → `DatasourceService` |
| 服务接入层·运维监控 | `platform-access` → `OpsMonitorService` |
| 服务接入层·告警设置 | `platform-access` → `AlarmService` |
| 横切·Permission / Audit / Crypto | `platform-spi` + `platform-core.security` |
| 编排·对象/数据/同步/校验/切换 | `platform-control.workflow.*Stage` + `WorkflowEngine` |
| 组件·DBCat | `runtime.component.DbCatComponent` |
| 组件·Store | `runtime.component.StoreComponent` |
| 组件·FullImport | `runtime.component.DefaultFullImporter` + `SyncWorker` |
| 组件·IncrSync | `runtime.component.IncrSyncComponent` |
| 组件·FullVerification | `runtime.component.FullVerificationComponent` |
| 组件·AIEngine | `runtime.component.AiEngineComponent` |
| Supervisor | `control.supervisor.DefaultSupervisor` |
| 控制面/数据面 | `platform-control` / `platform-runtime` |

## 模块依赖（单向）

```text
bootstrap
  → access, control, runtime, plugins-demo
access → control, core, spi
control → component-api, core, spi
runtime → component-api, core, spi
component-api → core, spi
core → spi
```

## 构建与运行

```bash
cd platform
mvn -q clean test package
java -jar platform-bootstrap/target/platform-bootstrap-0.1.0-SNAPSHOT.jar
```

Demo 按图 3-2 跑通：接入层建项目 → 安全审计 → 编排五阶段（对象→全量→增量→校验→切换）→ Supervisor 心跳。

## 新增可拔插组件/插件

1. 实现组件接口（`SchemaMigrator` / `FullImporter` / …）或 Reader/Writer SPI  
2. 插件放 `plugin.json` + jar，由 `PluginContainer.loadDirectory` 加载  
3. 在 `WorkflowEngine` 注册对应 `StageHandler`

> 骨架不含真实 JDBC/CDC；Crypto 为 Demo 占位，不可用于生产。
