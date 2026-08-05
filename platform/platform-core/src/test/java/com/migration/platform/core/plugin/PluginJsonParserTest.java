package com.migration.platform.core.plugin;

import com.migration.platform.spi.plugin.PluginDescriptor;
import com.migration.platform.spi.plugin.PluginType;
import org.junit.Assert;
import org.junit.Test;

public class PluginJsonParserTest {

    @Test
    public void parsePluginJson() {
        String json = "{\n"
                + "  \"name\": \"demo-memory-reader\",\n"
                + "  \"type\": \"READER\",\n"
                + "  \"class\": \"com.example.Reader\",\n"
                + "  \"version\": \"0.1.0\"\n"
                + "}";
        PluginDescriptor descriptor = PluginJsonParser.parse(json);
        Assert.assertEquals("demo-memory-reader", descriptor.getName());
        Assert.assertEquals(PluginType.READER, descriptor.getType());
        Assert.assertEquals("com.example.Reader", descriptor.getClassName());
        Assert.assertEquals("READER:demo-memory-reader", descriptor.key());
    }
}
