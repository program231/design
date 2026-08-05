package com.migration.platform.control.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory node registry (replaceable with ZK/etcd later).
 */
public class InMemoryNodeRegistry {

    private final Map<String, NodeInfo> nodes = new ConcurrentHashMap<String, NodeInfo>();

    public void register(NodeInfo node) {
        node.setLastHeartbeatMs(System.currentTimeMillis());
        nodes.put(node.getNodeId(), node);
    }

    public void heartbeat(String nodeId) {
        NodeInfo node = nodes.get(nodeId);
        if (node != null) {
            node.setLastHeartbeatMs(System.currentTimeMillis());
        }
    }

    public List<NodeInfo> listAlive(long timeoutMs) {
        long now = System.currentTimeMillis();
        List<NodeInfo> alive = new ArrayList<NodeInfo>();
        for (NodeInfo node : nodes.values()) {
            if (now - node.getLastHeartbeatMs() <= timeoutMs) {
                alive.add(node);
            }
        }
        return alive;
    }

    public NodeInfo get(String nodeId) {
        return nodes.get(nodeId);
    }
}
