package com.migration.platform.core.plugin;

import com.migration.platform.spi.plugin.PluginDescriptor;
import com.migration.platform.spi.plugin.PluginType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal JSON object parser for plugin.json (no external dependency).
 */
public final class PluginJsonParser {

    private static final Pattern STRING_FIELD = Pattern.compile("\"(\\w+)\"\\s*:\\s*\"([^\"]*)\"");

    private PluginJsonParser() {
    }

    public static PluginDescriptor parse(String json) {
        Map<String, String> fields = new HashMap<String, String>();
        Matcher matcher = STRING_FIELD.matcher(json);
        while (matcher.find()) {
            fields.put(matcher.group(1), matcher.group(2));
        }
        PluginDescriptor descriptor = new PluginDescriptor();
        descriptor.setName(required(fields, "name"));
        descriptor.setClassName(required(fields, "class"));
        descriptor.setVersion(fields.containsKey("version") ? fields.get("version") : "0.0.0");
        descriptor.setType(PluginType.valueOf(required(fields, "type").toUpperCase()));
        Map<String, String> props = new HashMap<String, String>();
        for (Map.Entry<String, String> e : fields.entrySet()) {
            if (!"name".equals(e.getKey()) && !"class".equals(e.getKey())
                    && !"type".equals(e.getKey()) && !"version".equals(e.getKey())) {
                props.put(e.getKey(), e.getValue());
            }
        }
        descriptor.setProperties(props);
        return descriptor;
    }

    private static String required(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("plugin.json missing field: " + key);
        }
        return value;
    }
}
