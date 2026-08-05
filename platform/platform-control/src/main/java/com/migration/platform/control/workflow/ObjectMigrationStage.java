package com.migration.platform.control.workflow;

import com.migration.platform.component.SchemaMigrator;

import java.util.Map;

/** 流程编排：数据库对象迁移 → 调度 DBCat. */
public class ObjectMigrationStage implements StageHandler {

    private final SchemaMigrator dbCat;

    public ObjectMigrationStage(SchemaMigrator dbCat) {
        this.dbCat = dbCat;
    }

    @Override
    public MigrationStage stage() {
        return MigrationStage.OBJECT_MIGRATION;
    }

    @Override
    public StageResult execute(Map<String, String> context) throws Exception {
        dbCat.start(context);
        dbCat.migrateSchema(context);
        dbCat.stop();
        return StageResult.ok("object migration done via DBCat");
    }
}
