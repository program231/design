package com.migration.platform.runtime.component;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.SchemaMigrator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/** 图3-2 组件层：DBCat 转换引擎骨架. */
public class DbCatComponent implements SchemaMigrator {

    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);

    @Override
    public String name() {
        return "DBCat";
    }

    @Override
    public void start(Map<String, String> params) {
        status.set(ComponentStatus.RUNNING);
    }

    @Override
    public void stop() {
        status.set(ComponentStatus.STOPPED);
    }

    @Override
    public ComponentStatus status() {
        return status.get();
    }

    @Override
    public void migrateSchema(Map<String, String> params) {
        // 骨架：采集/映射/DDL 执行后续由 TypeMapper 插件填充
        status.set(ComponentStatus.SUCCESS);
    }
}
