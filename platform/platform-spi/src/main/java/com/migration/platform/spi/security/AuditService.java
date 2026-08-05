package com.migration.platform.spi.security;

import java.util.Map;

/**
 * Audit SPI.
 */
public interface AuditService {

    void record(AuditEvent event);

    class AuditEvent {
        private String tenantId;
        private String userId;
        private String action;
        private String resourceType;
        private String resourceId;
        private boolean success;
        private long timestampMs;
        private Map<String, String> context;

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public long getTimestampMs() {
            return timestampMs;
        }

        public void setTimestampMs(long timestampMs) {
            this.timestampMs = timestampMs;
        }

        public Map<String, String> getContext() {
            return context;
        }

        public void setContext(Map<String, String> context) {
            this.context = context;
        }
    }
}
