package com.migration.platform.control.workflow;

import java.util.Map;

/**
 * 编排阶段统一契约（不直接读写业务数据）.
 */
public interface StageHandler {

    MigrationStage stage();

    StageResult execute(Map<String, String> context) throws Exception;
}
