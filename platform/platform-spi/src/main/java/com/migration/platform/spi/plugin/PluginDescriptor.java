package com.migration.platform.spi.plugin;

import java.util.Collections;
import java.util.Map;

/**
 * Descriptor parsed from plugin.json.
 */
public class PluginDescriptor {

    private String name;
    private PluginType type;
    private String className;
    private String version;
    private Map<String, String> properties = Collections.emptyMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PluginType getType() {
        return type;
    }

    public void setType(PluginType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties == null ? Collections.<String, String>emptyMap() : properties;
    }

    public String key() {
        return type.name() + ":" + name;
    }
}
