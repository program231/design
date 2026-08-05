package com.migration.platform.control.node;

import java.io.Serializable;

public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nodeId;
    private String host;
    private int port;
    private String role;
    private long lastHeartbeatMs;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getLastHeartbeatMs() {
        return lastHeartbeatMs;
    }

    public void setLastHeartbeatMs(long lastHeartbeatMs) {
        this.lastHeartbeatMs = lastHeartbeatMs;
    }
}
