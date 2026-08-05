package com.migration.platform.runtime.component;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.FullImporter;
import com.migration.platform.core.config.JobConfig;
import com.migration.platform.core.plugin.PluginContainer;
import com.migration.platform.core.stats.Communication;
import com.migration.platform.runtime.sync.SyncWorker;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultFullImporter implements FullImporter {

    private final SyncWorker syncWorker;
    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);

    public DefaultFullImporter(PluginContainer pluginContainer) {
        this.syncWorker = new SyncWorker(pluginContainer);
    }

    @Override
    public String name() {
        return "FullImport";
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
    public Communication runFullImport(JobConfig config) throws Exception {
        status.set(ComponentStatus.RUNNING);
        try {
            Communication communication = syncWorker.execute(config);
            status.set(ComponentStatus.SUCCESS);
            return communication;
        } catch (Exception e) {
            status.set(ComponentStatus.FAILED);
            throw e;
        }
    }
}
