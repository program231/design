package com.migration.platform.spi.plugin;

import com.migration.platform.spi.record.ColumnRecord;

/**
 * Full-load Writer SPI with Job/Task lifecycle.
 */
public interface WriterPlugin extends Plugin {

    Job createJob();

    interface Job {
        void init(PluginContext context);

        void destroy();
    }

    interface Task {
        void init(PluginContext context);

        void startWrite(RecordReceiver receiver);

        void destroy();
    }

    interface RecordReceiver {
        ColumnRecord getFromReader();
    }

    Task createTask();
}
