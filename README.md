# design

迁移工具设计文档

## 主交付文档

| 文档 | 格式 | 说明 |
|------|------|------|
| **[迁移平台软件设计说明书](doc/迁移平台软件设计说明书.md)** | Markdown | **主交付文档（SDD）**：符合软件设计说明书规范，含架构图、模块图、18 模块详细设计、流程设计思路 |
| **[迁移平台软件设计说明书](doc/迁移平台软件设计说明书.docx)** | Word | 可评审/归档的 Word 版，已嵌入架构图与模块图 PNG |

## 参考与设计底稿

| 文档 | 说明 |
|------|------|
| [迁移平台总体设计](doc/迁移平台总体设计.md) | 设计研究底稿，总体架构与能力映射 |
| [OMS 架构参考](doc/OMS架构参考.md) | OceanBase OMS 架构深度参考（从总体设计附录拆出） |
| [DataX 设计框架与模块分析](doc/DataX设计框架与模块分析.docx) | Alibaba DataX 框架分析 |
| [Flink 设计框架与模块总结](doc/Flink设计框架与模块总结.docx) | Apache Flink 框架分析 |

## 架构图资源

SDD 引用的 PNG 架构图位于 [`doc/images/`](doc/images/)，Mermaid 源文件位于 [`doc/diagrams/`](doc/diagrams/)。

| 图号 | 文件 | 内容 |
|------|------|------|
| 图3-1 | `fig3-1-logical-architecture.png` | 系统逻辑架构图 |
| 图3-2 | `fig3-2-module-composition.png` | 模块组成图 |
| 图3-3 | `fig3-3-control-data-plane.png` | 控制面与数据面分离 |
| 图3-4 | `fig3-4-single-node-deploy.png` | 单机部署架构 |
| 图3-5 | `fig3-5-multi-node-ha.png` | 多节点 HA 部署 |
| 图5-1 | `fig5-1-migration-flow.png` | 端到端迁移流程 |
| 图5-2 | `fig5-2-state-machine.png` | 任务状态机 |
| 图5-3 | `fig5-3-data-flow.png` | 全量与增量数据流 |

## 设计思路

本平台参考三套系统的设计精华：

- **DataX** — 全量离线同步：Framework + Plugin、Reader-Channel-Writer 管道
- **Flink CDC** — 增量实时同步：分布式运行时、Checkpoint 容错、Connector SPI
- **OceanBase OMS** — 产品化迁移流程：结构 → 全量 → 增量 → 校验 → 切换

详细模块设计与流程设计请参阅 [迁移平台软件设计说明书](doc/迁移平台软件设计说明书.md)。
