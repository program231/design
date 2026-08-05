package com.migration.platform.runtime.component;

import com.migration.platform.component.ComponentStatus;
import com.migration.platform.component.Verifier;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/** 图3-2 组件层：FullVerification 全量校验骨架. */
public class FullVerificationComponent implements Verifier {

    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);

    @Override
    public String name() {
        return "FullVerification";
    }

    @Override
    public void start(Map<String, String> params) {
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
    public VerifyReport verify(Map<String, String> params) {
        VerifyReport report = new VerifyReport();
        report.setCompared(50L);
        report.setDiffs(0L);
        report.setSamples(Collections.<String>emptyList());
        status.set(ComponentStatus.SUCCESS);
        return report;
    }
}
