package com.migration.platform.spi.plugin;

import com.migration.platform.spi.record.ChangeRecord;

/**
 * Source change-log adapter for Store.
 */
public interface LogAdapterPlugin extends Plugin {

    LogReader createReader();

    interface LogReader {
        void open(PluginContext context, String startPosition);

        ChangeRecord poll();

        String currentPosition();

        void close();
    }
}
