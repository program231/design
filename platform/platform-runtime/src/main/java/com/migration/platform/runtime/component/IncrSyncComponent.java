package com.migration.platform.runtime.component;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.IncrApplier;
import com.migration.platform.spi.record.ChangeRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/** 图3-2 组件层：IncrSync 增量同步骨架. */
public class IncrSyncComponent implements IncrApplier {

    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);
    private final List<ChangeRecord> applied = new ArrayList<ChangeRecord>();

    @Override
    public String name() {
        return "IncrSync";
    }

    @Override
    public void start(Map<String, String> params) {
        applied.clear();
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
    public void apply(ChangeRecord record) {
        applied.add(record);
    }

    @Override
    public long lagMs() {
        return 0L;
    }

    public int appliedSize() {
        return applied.size();
    }
}
