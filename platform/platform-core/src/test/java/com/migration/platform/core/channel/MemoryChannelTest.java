package com.migration.platform.core.channel;

import com.migration.platform.spi.record.ColumnRecord;
import org.junit.Assert;
import org.junit.Test;

public class MemoryChannelTest {

    @Test
    public void pushPullAndClose() throws Exception {
        MemoryChannel channel = new MemoryChannel(16, 1024 * 1024);
        ColumnRecord record = new ColumnRecord();
        record.put("id", 1);
        channel.push(record);
        Assert.assertEquals(1, channel.pull().get("id"));
        channel.close();
        Assert.assertTrue(channel.isClosed());
        Assert.assertNull(channel.pull());
    }
}
