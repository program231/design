package com.migration.platform.control.workflow;

public class StageResult {

    private final boolean success;
    private final String message;

    public StageResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static StageResult ok(String message) {
        return new StageResult(true, message);
    }

    public static StageResult fail(String message) {
        return new StageResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
