package com.migration.platform.plugins.demo;

import com.migration.platform.spi.plugin.PluginContext;
import com.migration.platform.spi.plugin.PluginType;
import com.migration.platform.spi.plugin.ReaderPlugin;
import com.migration.platform.spi.record.ColumnRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * In-memory demo reader generating sequential rows.
 */
public class DemoMemoryReader implements ReaderPlugin {

    @Override
    public String name() {
        return "demo-memory-reader";
    }

    @Override
    public PluginType type() {
        return PluginType.READER;
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
            }

            @Override
            public List<Map<String, String>> split(int adviceNumber) {
                return Collections.singletonList(Collections.<String, String>emptyMap());
            }

            @Override
            public void destroy() {
            }
        };
    }

    @Override
    public Task createTask() {
        return new Task() {
            private int rows = 100;

            @Override
            public void init(PluginContext context) {
                String configured = context.get("rows", "100");
                rows = Integer.parseInt(configured);
            }

            @Override
            public void startRead(RecordSender sender) {
                for (int i = 1; i <= rows; i++) {
                    ColumnRecord record = new ColumnRecord();
                    record.put("id", i);
                    record.put("name", "row-" + i);
                    sender.send(record);
                }
                sender.terminate();
            }

            @Override
            public void destroy() {
            }
        };
    }
}
