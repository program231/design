package com.migration.platform.component;

import com.migration.platform.spi.record.ChangeRecord;

public interface IncrApplier extends MigrationComponent {

    void apply(ChangeRecord record) throws Exception;

    long lagMs();
}
