package com.migration.platform.control.workflow;

import java.util.Map;

/** 流程编排：链路切换. */
public class LinkSwitchStage implements StageHandler {

    @Override
    public MigrationStage stage() {
        return MigrationStage.LINK_SWITCH;
    }

    @Override
    public StageResult execute(Map<String, String> context) {
        // 骨架：记录切换意图，真实 DNS/连接串切换后续实现
        String cutover = context.containsKey("cutoverTarget") ? context.get("cutoverTarget") : "target";
        return StageResult.ok("link switched to " + cutover);
    }
}
