package com.migration.platform.component;

import java.util.Map;

/**
 * Common lifecycle for executable migration components.
 */
public interface MigrationComponent {

    String name();

    void start(Map<String, String> params) throws Exception;

    void stop() throws Exception;

    ComponentStatus status();
}
