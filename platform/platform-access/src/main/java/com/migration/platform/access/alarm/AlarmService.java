package com.migration.platform.access.alarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 图3-2 服务接入层：告警设置.
 */
public class AlarmService {

    private final List<AlarmRule> rules = new CopyOnWriteArrayList<AlarmRule>();
    private final List<AlarmEvent> events = new CopyOnWriteArrayList<AlarmEvent>();

    public void addRule(String name, String metric, double threshold) {
        AlarmRule rule = new AlarmRule();
        rule.setName(name);
        rule.setMetric(metric);
        rule.setThreshold(threshold);
        rules.add(rule);
    }

    public void fire(String ruleName, String message) {
        AlarmEvent event = new AlarmEvent();
        event.setRuleName(ruleName);
        event.setMessage(message);
        event.setTimestampMs(System.currentTimeMillis());
        events.add(event);
    }

    public List<AlarmRule> rules() {
        return Collections.unmodifiableList(new ArrayList<AlarmRule>(rules));
    }

    public List<AlarmEvent> events() {
        return Collections.unmodifiableList(new ArrayList<AlarmEvent>(events));
    }

    public static class AlarmRule {
        private String name;
        private String metric;
        private double threshold;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMetric() {
            return metric;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }
    }

    public static class AlarmEvent {
        private String ruleName;
        private String message;
        private long timestampMs;

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestampMs() {
            return timestampMs;
        }

        public void setTimestampMs(long timestampMs) {
            this.timestampMs = timestampMs;
        }
    }
}
