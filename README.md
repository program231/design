# design

迁移工具设计文档

## 文档目录

| 文档 | 说明 |
|------|------|
| [迁移平台总体设计](doc/迁移平台总体设计.md) | 通用异构数据迁移平台总体架构设计，融合 DataX、Flink CDC 与 OceanBase OMS；附录 C 含 OMS 架构深度参考 |
| [DataX 设计框架与模块分析](doc/DataX设计框架与模块分析.docx) | Alibaba DataX 框架核心与 70 个插件模块的设计分析 |
| [Flink 设计框架与模块总结](doc/Flink设计框架与模块总结.docx) | Apache Flink 2.1-SNAPSHOT 分层架构与模块设计总结 |

## 设计思路

本平台参考三套系统的设计精华：

- **DataX** — 全量离线同步：Framework + Plugin、Reader-Channel-Writer 管道
- **Flink CDC** — 增量实时同步：分布式运行时、Checkpoint 容错、Connector SPI
- **OceanBase OMS** — 产品化迁移流程：结构 → 全量 → 增量 → 校验 → 切换

详细架构与模块设计请参阅 [迁移平台总体设计](doc/迁移平台总体设计.md)。
