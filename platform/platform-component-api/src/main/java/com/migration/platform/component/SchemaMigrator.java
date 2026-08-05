package com.migration.platform.component;

import java.util.Map;

public interface SchemaMigrator extends MigrationComponent {

    /**
     * Collect source schema, map types, apply DDL on target.
     */
    void migrateSchema(Map<String, String> params) throws Exception;
}
