package com.migration.platform.runtime.component;

import com.migration.platform.component.AiAssistant;
import com.migration.platform.component.ComponentStatus;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/** 图3-2 组件层：AIEngine 智能辅助骨架. */
public class AiEngineComponent implements AiAssistant {

    private final AtomicReference<ComponentStatus> status =
            new AtomicReference<ComponentStatus>(ComponentStatus.IDLE);

    @Override
    public String name() {
        return "AIEngine";
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
    public String suggestMapping(Map<String, String> context) {
        return "suggest: map source.type -> target.compatible_type";
    }

    @Override
    public String diagnose(Map<String, String> context) {
        return "diagnose: check network/permission/checkpoint lag";
    }
}
