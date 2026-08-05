package com.migration.platform.control.supervisor;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.MigrationComponent;
import com.migration.platform.component.Supervisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSupervisor implements Supervisor {

    private final Map<String, MigrationComponent> components = new ConcurrentHashMap<String, MigrationComponent>();

    @Override
    public void register(MigrationComponent component) {
        components.put(component.name(), component);
    }

    @Override
    public void unregister(String componentName) {
        components.remove(componentName);
    }

    @Override
    public List<Heartbeat> collect() {
        long now = System.currentTimeMillis();
        List<Heartbeat> list = new ArrayList<Heartbeat>();
        for (MigrationComponent component : components.values()) {
            ComponentStatus status = component.status();
            list.add(new Heartbeat(component.name(), status, now));
        }
        return list;
    }
}
