package com.migration.platform.spi.plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Runtime context passed into plugins.
 */
public class PluginContext {

    private final Map<String, String> configuration;

    public PluginContext(Map<String, String> configuration) {
        this.configuration = configuration == null
                ? Collections.<String, String>emptyMap()
                : Collections.unmodifiableMap(new HashMap<String, String>(configuration));
    }

    public String get(String key) {
        return configuration.get(key);
    }

    public String get(String key, String defaultValue) {
        String value = configuration.get(key);
        return value == null ? defaultValue : value;
    }

    public Map<String, String> asMap() {
        return configuration;
    }
}
