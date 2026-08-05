package com.migration.platform.spi.record;

public class HeartbeatRecord extends ChangeRecord {

    private static final long serialVersionUID = 1L;

    @Override
    public RecordKind kind() {
        return RecordKind.HEARTBEAT;
    }
}
