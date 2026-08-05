package com.migration.platform.control.meta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory meta store for task config / checkpoint placeholders.
 */
public class InMemoryMetaStore {

    private final Map<String, String> kv = new ConcurrentHashMap<String, String>();

    public void put(String key, String value) {
        kv.put(key, value);
    }

    public String get(String key) {
        return kv.get(key);
    }

    public void putPosition(String taskId, String position) {
        put("position:" + taskId, position);
    }

    public String getPosition(String taskId) {
        return get("position:" + taskId);
    }
}
