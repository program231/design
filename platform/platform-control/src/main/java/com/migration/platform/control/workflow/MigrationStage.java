package com.migration.platform.control.workflow;

/**
 * 图3-2 流程编排层阶段枚举.
 */
public enum MigrationStage {
    OBJECT_MIGRATION,
    DATA_MIGRATION,
    DATA_SYNC,
    DATA_VERIFICATION,
    LINK_SWITCH
}
