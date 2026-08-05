package com.migration.platform.control.workflow;

import com.migration.platform.component.Verifier;

import java.util.Map;

/** 流程编排：数据校验 → FullVerification. */
public class DataVerificationStage implements StageHandler {

    private final Verifier verifier;

    public DataVerificationStage(Verifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public MigrationStage stage() {
        return MigrationStage.DATA_VERIFICATION;
    }

    @Override
    public StageResult execute(Map<String, String> context) throws Exception {
        verifier.start(context);
        Verifier.VerifyReport report = verifier.verify(context);
        verifier.stop();
        return StageResult.ok("compared=" + report.getCompared() + ", diffs=" + report.getDiffs());
    }
}
