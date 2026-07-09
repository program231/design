# design

迁移工具设计文档

## 主交付文档

| 文档 | 格式 | 说明 |
|------|------|------|
| **[迁移平台软件设计说明书](doc/迁移平台软件设计说明书.md)** | Markdown | **主交付文档（SDD v2.0）**：自研迁移平台设计，借鉴 DataX/Flink/OMS 架构思想 |
| **[迁移平台软件设计说明书](doc/迁移平台软件设计说明书.docx)** | Word | 可评审/归档的 Word 版，已嵌入架构图 PNG |

> **文档定位**：设计一套**自研一体化迁移平台**，借鉴 OMS 产品流程、DataX 全量管道模型、Flink 流式运行时设计，**非 DataX/Flink 集成调用方案**。

## 参考与设计底稿

| 文档 | 说明 |
|------|------|
| [迁移平台总体设计](doc/迁移平台总体设计.md) | 设计研究底稿 |
| [OMS 架构参考](doc/OMS架构参考.md) | OceanBase OMS 架构深度参考 |
| [DataX 设计框架与模块分析](doc/DataX设计框架与模块分析.docx) | **设计借鉴来源**：Framework+Plugin、Reader-Channel-Writer |
| [Flink 设计框架与模块总结](doc/Flink设计框架与模块总结.docx) | **设计借鉴来源**：分布式运行时、Checkpoint、Connector SPI |

## 架构图资源

[`doc/images/`](doc/images/) — SDD 引用的 8 张 PNG 架构图

## 设计思路

| 参考系统 | 借鉴的设计思想 | 本平台对应模块 |
|----------|----------------|----------------|
| **OceanBase OMS** | 产品化迁移流程、DBCat/Store/IncrSync 组件、HA | 控制台、工作流、执行组件 |
| **DataX** | Framework+Plugin、Reader-Channel-Writer、Task 调度 | FullImport、SyncWorker、Reader/Writer 插件 |
| **Apache Flink** | JobManager/TaskManager、Checkpoint、Connector SPI | StreamRuntime、IncrSync、LogAdapter 插件 |

详细设计请参阅 [迁移平台软件设计说明书](doc/迁移平台软件设计说明书.md)。
