package com.migration.platform.component;

import com.migration.platform.core.config.JobConfig;
import com.migration.platform.core.stats.Communication;

public interface FullImporter extends MigrationComponent {

    Communication runFullImport(JobConfig config) throws Exception;
}
