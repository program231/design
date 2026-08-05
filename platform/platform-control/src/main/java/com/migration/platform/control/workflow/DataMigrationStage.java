package com.migration.platform.control.workflow;

import com.migration.platform.component.FullImporter;
import com.migration.platform.core.config.JobConfig;
import com.migration.platform.core.stats.Communication;

import java.util.HashMap;
import java.util.Map;

/** 流程编排：数据迁移 → 调度 FullImport. */
public class DataMigrationStage implements StageHandler {

    private final FullImporter fullImporter;

    public DataMigrationStage(FullImporter fullImporter) {
        this.fullImporter = fullImporter;
    }

    @Override
    public MigrationStage stage() {
        return MigrationStage.DATA_MIGRATION;
    }

    @Override
    public StageResult execute(Map<String, String> context) throws Exception {
        JobConfig config = new JobConfig();
        config.setTaskId(context.get("taskId"));
        config.setJobId(context.get("jobId"));
        config.setReaderName(context.get("readerName"));
        config.setWriterName(context.get("writerName"));
        Map<String, String> readerParams = new HashMap<String, String>();
        if (context.containsKey("rows")) {
            readerParams.put("rows", context.get("rows"));
        }
        config.setReaderParams(readerParams);
        fullImporter.start(context);
        Communication communication = fullImporter.runFullImport(config);
        fullImporter.stop();
        return StageResult.ok("full import records=" + communication.totalRecords());
    }
}
