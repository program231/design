package com.migration.platform.spi.record;

import java.io.Serializable;

public abstract class ChangeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String schema;
    private String table;
    private long timestampMs;
    private String position;

    public abstract RecordKind kind();

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
