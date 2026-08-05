package com.migration.platform.component;

import java.util.Map;

public interface AiAssistant extends MigrationComponent {

    String suggestMapping(Map<String, String> context);

    String diagnose(Map<String, String> context);
}
