package com.migration.platform.core.security;

import com.migration.platform.spi.security.AuditService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory audit sink for local demo / tests.
 */
public class InMemoryAuditService implements AuditService {

    private final List<AuditEvent> events = new CopyOnWriteArrayList<AuditEvent>();

    @Override
    public void record(AuditEvent event) {
        if (event.getTimestampMs() <= 0L) {
            event.setTimestampMs(System.currentTimeMillis());
        }
        events.add(event);
    }

    public List<AuditEvent> snapshot() {
        return Collections.unmodifiableList(new ArrayList<AuditEvent>(events));
    }

    public void clear() {
        events.clear();
    }
}
