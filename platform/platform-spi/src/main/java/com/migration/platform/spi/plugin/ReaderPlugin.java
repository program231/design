package com.migration.platform.spi.plugin;

import com.migration.platform.spi.record.ColumnRecord;

import java.util.List;
import java.util.Map;

/**
 * Full-load Reader SPI with Job/Task lifecycle.
 */
public interface ReaderPlugin extends Plugin {

    Job createJob();

    interface Job {
        void init(PluginContext context);

        List<Map<String, String>> split(int adviceNumber);

        void destroy();
    }

    interface Task {
        void init(PluginContext context);

        void startRead(RecordSender sender);

        void destroy();
    }

    interface RecordSender {
        void send(ColumnRecord record);

        void terminate();
    }

    Task createTask();
}
