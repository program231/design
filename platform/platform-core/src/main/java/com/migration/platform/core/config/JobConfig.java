package com.migration.platform.core.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Job specification shared by control and runtime.
 */
public class JobConfig {

    private String jobId;
    private String taskId;
    private String readerName;
    private String writerName;
    private int channelCapacity = 1024;
    private long channelMaxBytes = 32L * 1024L * 1024L;
    private int concurrency = 1;
    private Map<String, String> readerParams = new HashMap<String, String>();
    private Map<String, String> writerParams = new HashMap<String, String>();

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public int getChannelCapacity() {
        return channelCapacity;
    }

    public void setChannelCapacity(int channelCapacity) {
        this.channelCapacity = channelCapacity;
    }

    public long getChannelMaxBytes() {
        return channelMaxBytes;
    }

    public void setChannelMaxBytes(long channelMaxBytes) {
        this.channelMaxBytes = channelMaxBytes;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public Map<String, String> getReaderParams() {
        return readerParams;
    }

    public void setReaderParams(Map<String, String> readerParams) {
        this.readerParams = readerParams == null ? new HashMap<String, String>() : new HashMap<String, String>(readerParams);
    }

    public Map<String, String> getWriterParams() {
        return writerParams;
    }

    public void setWriterParams(Map<String, String> writerParams) {
        this.writerParams = writerParams == null ? new HashMap<String, String>() : new HashMap<String, String>(writerParams);
    }

    public Map<String, String> readerContext() {
        return Collections.unmodifiableMap(readerParams);
    }

    public Map<String, String> writerContext() {
        return Collections.unmodifiableMap(writerParams);
    }
}
