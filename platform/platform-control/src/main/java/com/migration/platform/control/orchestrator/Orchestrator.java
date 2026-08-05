package com.migration.platform.control.orchestrator;

import com.migration.platform.control.meta.InMemoryMetaStore;
import com.migration.platform.control.schedule.JobDispatcher;
import com.migration.platform.core.config.JobConfig;
import com.migration.platform.spi.security.AuditService;
import com.migration.platform.spi.security.PermissionService;

import java.util.UUID;

/**
 * Control-plane orchestrator skeleton.
 */
public class Orchestrator {

    private final PermissionService permissionService;
    private final AuditService auditService;
    private final InMemoryMetaStore metaStore;
    private final JobDispatcher dispatcher;

    public Orchestrator(PermissionService permissionService,
                        AuditService auditService,
                        InMemoryMetaStore metaStore,
                        JobDispatcher dispatcher) {
        this.permissionService = permissionService;
        this.auditService = auditService;
        this.metaStore = metaStore;
        this.dispatcher = dispatcher;
    }

    public String createAndStartFullJob(String tenantId, String userId, JobConfig config) {
        boolean allowed = permissionService.check(tenantId, userId, "TASK", config.getTaskId(), "START");
        AuditService.AuditEvent event = new AuditService.AuditEvent();
        event.setTenantId(tenantId);
        event.setUserId(userId);
        event.setAction("START_FULL_JOB");
        event.setResourceType("TASK");
        event.setResourceId(config.getTaskId());
        event.setSuccess(allowed);
        auditService.record(event);
        if (!allowed) {
            throw new SecurityException("permission denied");
        }
        if (config.getJobId() == null) {
            config.setJobId(UUID.randomUUID().toString());
        }
        metaStore.put("task:" + config.getTaskId() + ":state", "RUNNING");
        metaStore.putPosition(config.getTaskId(), "full-start");
        JobDispatcher.DispatchedJob dispatched = dispatcher.dispatch(config);
        return dispatched.getDispatchId();
    }
}
