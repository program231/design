package com.migration.platform.spi.record;

public class DdlRecord extends ChangeRecord {

    private static final long serialVersionUID = 1L;

    private String ddlSql;

    @Override
    public RecordKind kind() {
        return RecordKind.DDL;
    }

    public String getDdlSql() {
        return ddlSql;
    }

    public void setDdlSql(String ddlSql) {
        this.ddlSql = ddlSql;
    }
}
