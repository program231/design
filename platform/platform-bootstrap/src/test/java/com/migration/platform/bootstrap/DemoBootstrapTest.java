package com.migration.platform.bootstrap;

import com.migration.platform.plugins.demo.DemoMemoryWriter;
import org.junit.Assert;
import org.junit.Test;

public class DemoBootstrapTest {

    @Test
    public void runDemoMain() throws Exception {
        DemoMemoryWriter.clearSink();
        DemoBootstrap.main(new String[0]);
        Assert.assertEquals(50, DemoMemoryWriter.sinkSnapshot().size());
    }
}
