package com.migration.platform.core.channel;

import com.migration.platform.spi.record.ColumnRecord;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryChannel implements Channel {

    private static final ColumnRecord POISON = new ColumnRecord();

    private final BlockingQueue<ColumnRecord> queue;
    private final long maxBytes;
    private final AtomicLong records = new AtomicLong();
    private final AtomicLong bytes = new AtomicLong();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public MemoryChannel(int capacity, long maxBytes) {
        this.queue = new ArrayBlockingQueue<ColumnRecord>(capacity);
        this.maxBytes = maxBytes;
    }

    @Override
    public void push(ColumnRecord record) throws InterruptedException {
        if (closed.get()) {
            throw new IllegalStateException("channel closed");
        }
        long estimate = estimateBytes(record);
        while (bytes.get() + estimate > maxBytes) {
            if (closed.get()) {
                throw new IllegalStateException("channel closed");
            }
            Thread.sleep(5L);
        }
        queue.put(record);
        records.incrementAndGet();
        bytes.addAndGet(estimate);
    }

    @Override
    public ColumnRecord pull() throws InterruptedException {
        while (true) {
            ColumnRecord record = queue.poll(200L, TimeUnit.MILLISECONDS);
            if (record == null) {
                if (closed.get() && queue.isEmpty()) {
                    return null;
                }
                continue;
            }
            if (record == POISON) {
                return null;
            }
            bytes.addAndGet(-estimateBytes(record));
            return record;
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            queue.offer(POISON);
        }
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public long recordCount() {
        return records.get();
    }

    @Override
    public long byteCount() {
        return Math.max(0L, bytes.get());
    }

    private long estimateBytes(ColumnRecord record) {
        if (record == null || record == POISON) {
            return 0L;
        }
        return Math.max(64L, record.size() * 32L);
    }
}
