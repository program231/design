package com.migration.platform.spi.record;

/**
 * Incremental change record kinds.
 */
public enum RecordKind {
    DML,
    DDL,
    HEARTBEAT
}
