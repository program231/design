package com.migration.platform.core.channel;

import com.migration.platform.spi.record.ColumnRecord;

/**
 * Bounded memory channel with record/byte back-pressure.
 */
public interface Channel {

    void push(ColumnRecord record) throws InterruptedException;

    ColumnRecord pull() throws InterruptedException;

    void close();

    boolean isClosed();

    long recordCount();

    long byteCount();
}
