package com.migration.platform.spi.record;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Full-load column oriented record.
 */
public class ColumnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Object> columns = new LinkedHashMap<String, Object>();

    public void put(String column, Object value) {
        columns.put(column, value);
    }

    public Object get(String column) {
        return columns.get(column);
    }

    public Map<String, Object> columns() {
        return columns;
    }

    public int size() {
        return columns.size();
    }

    @Override
    public String toString() {
        return "ColumnRecord" + columns;
    }
}
