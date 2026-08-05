package com.migration.platform.control.schedule;

import com.migration.platform.control.node.InMemoryNodeRegistry;
import com.migration.platform.control.node.NodeInfo;
import com.migration.platform.core.config.JobConfig;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simple job dispatcher separating control decision from data-plane execution.
 */
public class JobDispatcher {

    private final InMemoryNodeRegistry registry;
    private final BlockingQueue<DispatchedJob> queue = new LinkedBlockingQueue<DispatchedJob>();

    public JobDispatcher(InMemoryNodeRegistry registry) {
        this.registry = registry;
    }

    public DispatchedJob dispatch(JobConfig config) {
        List<NodeInfo> workers = registry.listAlive(30_000L);
        String targetNode = workers.isEmpty() ? "local" : workers.get(0).getNodeId();
        DispatchedJob job = new DispatchedJob();
        job.setDispatchId(UUID.randomUUID().toString());
        job.setTargetNodeId(targetNode);
        job.setConfig(config);
        queue.offer(job);
        return job;
    }

    public DispatchedJob take() throws InterruptedException {
        return queue.take();
    }

    public DispatchedJob poll() {
        return queue.poll();
    }

    public static class DispatchedJob {
        private String dispatchId;
        private String targetNodeId;
        private JobConfig config;

        public String getDispatchId() {
            return dispatchId;
        }

        public void setDispatchId(String dispatchId) {
            this.dispatchId = dispatchId;
        }

        public String getTargetNodeId() {
            return targetNodeId;
        }

        public void setTargetNodeId(String targetNodeId) {
            this.targetNodeId = targetNodeId;
        }

        public JobConfig getConfig() {
            return config;
        }

        public void setConfig(JobConfig config) {
            this.config = config;
        }
    }
}
