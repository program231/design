package com.migration.platform.spi.plugin;

/**
 * Marker for loadable plugins.
 */
public interface Plugin {

    String name();

    PluginType type();

    void init(PluginContext context);

    void destroy();
}
