package com.migration.platform.control.workflow;

import com.migration.platform.component.IncrApplier;
import com.migration.platform.component.LogStore;
import com.migration.platform.spi.record.ChangeRecord;
import com.migration.platform.spi.record.HeartbeatRecord;

import java.util.Map;

/** 流程编排：数据同步 → Store + IncrSync. */
public class DataSyncStage implements StageHandler {

    private final LogStore store;
    private final IncrApplier incrSync;

    public DataSyncStage(LogStore store, IncrApplier incrSync) {
        this.store = store;
        this.incrSync = incrSync;
    }

    @Override
    public MigrationStage stage() {
        return MigrationStage.DATA_SYNC;
    }

    @Override
    public StageResult execute(Map<String, String> context) throws Exception {
        store.start(context);
        incrSync.start(context);
        int max = 10;
        if (context.containsKey("maxIncrRecords")) {
            max = Integer.parseInt(context.get("maxIncrRecords"));
        }
        long applied = 0L;
        String checkpoint = store.currentPosition();
        for (int i = 0; i < max; i++) {
            ChangeRecord record = store.poll();
            if (record == null) {
                break;
            }
            if (!(record instanceof HeartbeatRecord)) {
                incrSync.apply(record);
                applied++;
            }
            if (record.getPosition() != null) {
                checkpoint = record.getPosition();
            }
        }
        incrSync.stop();
        store.stop();
        return StageResult.ok("incr applied=" + applied + ", checkpoint=" + checkpoint);
    }
}
