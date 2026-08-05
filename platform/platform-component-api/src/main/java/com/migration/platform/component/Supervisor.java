package com.migration.platform.component;

import java.util.List;

public interface Supervisor {

    void register(MigrationComponent component);

    void unregister(String componentName);

    List<Heartbeat> collect();

    class Heartbeat {
        private String componentName;
        private ComponentStatus status;
        private long timestampMs;

        public Heartbeat(String componentName, ComponentStatus status, long timestampMs) {
            this.componentName = componentName;
            this.status = status;
            this.timestampMs = timestampMs;
        }

        public String getComponentName() {
            return componentName;
        }

        public ComponentStatus getStatus() {
            return status;
        }

        public long getTimestampMs() {
            return timestampMs;
        }
    }
}
