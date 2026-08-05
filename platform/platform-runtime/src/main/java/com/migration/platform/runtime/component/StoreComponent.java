package com.migration.platform.runtime.component;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.LogStore;
import com.migration.platform.spi.record.ChangeRecord;
import com.migration.platform.spi.record.DmlRecord;
import com.migration.platform.spi.record.HeartbeatRecord;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/** 图3-2 组件层：Store 增量拉取骨架（内存演示队列）. */
public class StoreComponent implements LogStore {

    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);
    private final Deque<ChangeRecord> queue = new ArrayDeque<ChangeRecord>();
    private String position = "0";

    @Override
    public String name() {
        return "Store";
    }

    @Override
    public void start(Map<String, String> params) {
        queue.clear();
        DmlRecord dml = new DmlRecord();
        dml.setOp(DmlRecord.Op.INSERT);
        dml.setSchema("demo");
        dml.setTable("t1");
        dml.setPosition("1");
        Map<String, Object> after = new HashMap<String, Object>();
        after.put("id", 1);
        dml.setAfter(after);
        queue.add(dml);
        HeartbeatRecord hb = new HeartbeatRecord();
        hb.setPosition("1");
        queue.add(hb);
        position = "0";
        status.set(ComponentStatus.RUNNING);
    }

    @Override
    public void stop() {
        status.set(ComponentStatus.STOPPED);
    }

    @Override
    public ComponentStatus status() {
        return status.get();
    }

    @Override
    public ChangeRecord poll() {
        ChangeRecord record = queue.pollFirst();
        if (record != null && record.getPosition() != null) {
            position = record.getPosition();
        }
        return record;
    }

    @Override
    public String currentPosition() {
        return position;
    }
}
