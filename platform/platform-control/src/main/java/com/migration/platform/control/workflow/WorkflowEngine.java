package com.migration.platform.control.workflow;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 图3-2 流程编排层引擎：按阶段顺序推进.
 */
public class WorkflowEngine {

    private final Map<MigrationStage, StageHandler> handlers =
            new EnumMap<MigrationStage, StageHandler>(MigrationStage.class);

    public void register(StageHandler handler) {
        handlers.put(handler.stage(), handler);
    }

    public List<StageResult> runPipeline(Map<String, String> context, MigrationStage... stages) throws Exception {
        List<StageResult> results = new ArrayList<StageResult>();
        for (MigrationStage stage : stages) {
            StageHandler handler = handlers.get(stage);
            if (handler == null) {
                throw new IllegalStateException("no handler for stage: " + stage);
            }
            StageResult result = handler.execute(context);
            results.add(result);
            if (!result.isSuccess()) {
                break;
            }
        }
        return results;
    }
}
