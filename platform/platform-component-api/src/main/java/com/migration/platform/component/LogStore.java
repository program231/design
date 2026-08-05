package com.migration.platform.component;

import com.migration.platform.spi.record.ChangeRecord;

public interface LogStore extends MigrationComponent {

    ChangeRecord poll() throws Exception;

    String currentPosition();
}
