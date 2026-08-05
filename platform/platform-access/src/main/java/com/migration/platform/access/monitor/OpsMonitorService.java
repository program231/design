package com.migration.platform.access.monitor;

import com.migration.platform.core.stats.Communication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图3-2 服务接入层：运维监控.
 */
public class OpsMonitorService {

    private final Map<String, Communication> taskStats = new ConcurrentHashMap<String, Communication>();

    public void report(String taskId, Communication communication) {
        taskStats.put(taskId, communication);
    }

    public Communication get(String taskId) {
        return taskStats.get(taskId);
    }

    public List<String> listTaskIds() {
        return Collections.unmodifiableList(new ArrayList<String>(taskStats.keySet()));
    }
}
