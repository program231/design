package com.migration.platform.spi.record;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DmlRecord extends ChangeRecord {

    private static final long serialVersionUID = 1L;

    public enum Op {
        INSERT, UPDATE, DELETE
    }

    private Op op;
    private Map<String, Object> before = Collections.emptyMap();
    private Map<String, Object> after = Collections.emptyMap();

    @Override
    public RecordKind kind() {
        return RecordKind.DML;
    }

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    public Map<String, Object> getBefore() {
        return before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before == null ? Collections.<String, Object>emptyMap() : new LinkedHashMap<String, Object>(before);
    }

    public Map<String, Object> getAfter() {
        return after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after == null ? Collections.<String, Object>emptyMap() : new LinkedHashMap<String, Object>(after);
    }
}
