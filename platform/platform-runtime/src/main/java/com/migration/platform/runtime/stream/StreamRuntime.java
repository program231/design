package com.migration.platform.runtime.stream;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.IncrApplier;
import com.migration.platform.component.LogStore;
import com.migration.platform.spi.record.ChangeRecord;
import com.migration.platform.spi.record.HeartbeatRecord;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Incremental stream runtime skeleton with checkpoint placeholder.
 */
public class StreamRuntime {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<String> checkpoint = new AtomicReference<String>("0");
    private final AtomicLong applied = new AtomicLong();

    public void runOnce(LogStore store, IncrApplier applier, int maxRecords) throws Exception {
        running.set(true);
        try {
            for (int i = 0; i < maxRecords; i++) {
                ChangeRecord record = store.poll();
                if (record == null) {
                    break;
                }
                if (!(record instanceof HeartbeatRecord)) {
                    applier.apply(record);
                    applied.incrementAndGet();
                }
                if (record.getPosition() != null) {
                    checkpoint.set(record.getPosition());
                }
            }
        } finally {
            running.set(false);
        }
    }

    public String checkpoint() {
        return checkpoint.get();
    }

    public long appliedCount() {
        return applied.get();
    }

    public boolean isRunning() {
        return running.get();
    }

    /**
     * No-op incremental stubs for local wiring demos.
     */
    public static class NoopStore implements LogStore {
        private ComponentStatus status = ComponentStatus.IDLE;

        @Override
        public String name() {
            return "noop-store";
        }

        @Override
        public void start(Map<String, String> params) {
            status = ComponentStatus.RUNNING;
        }

        @Override
        public void stop() {
            status = ComponentStatus.STOPPED;
        }

        @Override
        public ComponentStatus status() {
            return status;
        }

        @Override
        public ChangeRecord poll() {
            return null;
        }

        @Override
        public String currentPosition() {
            return "0";
        }
    }

    public static class NoopApplier implements IncrApplier {
        private ComponentStatus status = ComponentStatus.IDLE;

        @Override
        public String name() {
            return "noop-applier";
        }

        @Override
        public void start(Map<String, String> params) {
            status = ComponentStatus.RUNNING;
        }

        @Override
        public void stop() {
            status = ComponentStatus.STOPPED;
        }

        @Override
        public ComponentStatus status() {
            return status;
        }

        @Override
        public void apply(ChangeRecord record) {
            // no-op
        }

        @Override
        public long lagMs() {
            return 0L;
        }
    }
}
