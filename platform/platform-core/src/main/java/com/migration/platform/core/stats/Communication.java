package com.migration.platform.core.stats;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Aggregates throughput and dirty counters.
 */
public class Communication {

    private final AtomicLong byteSpeed = new AtomicLong();
    private final AtomicLong recordSpeed = new AtomicLong();
    private final AtomicLong dirtyCount = new AtomicLong();
    private final long startMs = System.currentTimeMillis();

    public void addBytes(long n) {
        byteSpeed.addAndGet(n);
    }

    public void addRecords(long n) {
        recordSpeed.addAndGet(n);
    }

    public void addDirty(long n) {
        dirtyCount.addAndGet(n);
    }

    public long totalBytes() {
        return byteSpeed.get();
    }

    public long totalRecords() {
        return recordSpeed.get();
    }

    public long dirty() {
        return dirtyCount.get();
    }

    public double recordsPerSecond() {
        long elapsed = Math.max(1L, System.currentTimeMillis() - startMs);
        return totalRecords() * 1000.0d / elapsed;
    }

    @Override
    public String toString() {
        return "Communication{records=" + totalRecords()
                + ", bytes=" + totalBytes()
                + ", dirty=" + dirty()
                + ", rps=" + String.format("%.2f", recordsPerSecond())
                + "}";
    }
}
