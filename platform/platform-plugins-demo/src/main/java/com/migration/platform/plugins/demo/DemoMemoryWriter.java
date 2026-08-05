package com.migration.platform.plugins.demo;

import com.migration.platform.spi.plugin.PluginContext;
import com.migration.platform.spi.plugin.PluginType;
import com.migration.platform.spi.plugin.WriterPlugin;
import com.migration.platform.spi.record.ColumnRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory demo writer capturing rows for verification.
 */
public class DemoMemoryWriter implements WriterPlugin {

    private static final List<ColumnRecord> SINK = new CopyOnWriteArrayList<ColumnRecord>();

    public static List<ColumnRecord> sinkSnapshot() {
        return Collections.unmodifiableList(new ArrayList<ColumnRecord>(SINK));
    }

    public static void clearSink() {
        SINK.clear();
    }

    @Override
    public String name() {
        return "demo-memory-writer";
    }

    @Override
    public PluginType type() {
        return PluginType.WRITER;
    }

    @Override
    public void init(PluginContext context) {
        // no-op
    }

    @Override
    public void destroy() {
        // no-op
    }

    @Override
    public Job createJob() {
        return new Job() {
            @Override
            public void init(PluginContext context) {
                clearSink();
            }

            @Override
            public void destroy() {
            }
        };
    }

    @Override
    public Task createTask() {
        return new Task() {
            @Override
            public void init(PluginContext context) {
            }

            @Override
            public void startWrite(RecordReceiver receiver) {
                while (true) {
                    ColumnRecord record = receiver.getFromReader();
                    if (record == null) {
                        break;
                    }
                    SINK.add(record);
                }
            }

            @Override
            public void destroy() {
            }
        };
    }
}
