package com.migration.platform.bootstrap;

import com.migration.platform.access.alarm.AlarmService;
import com.migration.platform.access.datasource.DatasourceService;
import com.migration.platform.access.monitor.OpsMonitorService;
import com.migration.platform.access.project.TransferProjectService;
import com.migration.platform.component.AiAssistant;
import com.migration.platform.component.FullImporter;
import com.migration.platform.component.IncrApplier;
import com.migration.platform.component.LogStore;
import com.migration.platform.component.SchemaMigrator;
import com.migration.platform.component.Verifier;
import com.migration.platform.control.supervisor.DefaultSupervisor;
import com.migration.platform.control.workflow.DataMigrationStage;
import com.migration.platform.control.workflow.DataSyncStage;
import com.migration.platform.control.workflow.DataVerificationStage;
import com.migration.platform.control.workflow.LinkSwitchStage;
import com.migration.platform.control.workflow.MigrationStage;
import com.migration.platform.control.workflow.ObjectMigrationStage;
import com.migration.platform.control.workflow.StageResult;
import com.migration.platform.control.workflow.WorkflowEngine;
import com.migration.platform.core.plugin.PluginContainer;
import com.migration.platform.core.security.AllowAllPermissionService;
import com.migration.platform.core.security.DemoCryptoService;
import com.migration.platform.core.security.InMemoryAuditService;
import com.migration.platform.core.stats.Communication;
import com.migration.platform.plugins.demo.DemoMemoryReader;
import com.migration.platform.plugins.demo.DemoMemoryWriter;
import com.migration.platform.runtime.component.AiEngineComponent;
import com.migration.platform.runtime.component.DbCatComponent;
import com.migration.platform.runtime.component.DefaultFullImporter;
import com.migration.platform.runtime.component.FullVerificationComponent;
import com.migration.platform.runtime.component.IncrSyncComponent;
import com.migration.platform.runtime.component.StoreComponent;
import com.migration.platform.spi.plugin.PluginDescriptor;
import com.migration.platform.spi.plugin.PluginType;
import com.migration.platform.spi.security.AuditService;
import com.migration.platform.spi.security.CryptoService;
import com.migration.platform.spi.security.PermissionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按图3-2 组装：服务接入层 → 横切安全 → 流程编排层 → 组件层(+Supervisor).
 */
public final class DemoBootstrap {

    private DemoBootstrap() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Migration Platform Framework (aligned to design diagrams) ===");

        // --- 横切安全 Permission / Audit / Crypto ---
        PermissionService permission = new AllowAllPermissionService();
        InMemoryAuditService audit = new InMemoryAuditService();
        CryptoService crypto = new DemoCryptoService();

        // --- 服务接入层 ---
        TransferProjectService projectService = new TransferProjectService();
        DatasourceService datasourceService = new DatasourceService(crypto);
        OpsMonitorService monitorService = new OpsMonitorService();
        AlarmService alarmService = new AlarmService();
        alarmService.addRule("incr-lag", "lagMs", 60.0d);

        DatasourceService.Datasource source = datasourceService.register(
                "src-demo", "jdbc:demo:source", "u", "p", "default");
        DatasourceService.Datasource target = datasourceService.register(
                "tgt-demo", "jdbc:demo:target", "u", "p", "default");
        TransferProjectService.TransferProject project = projectService.create(
                "demo-project", source.getDatasourceId(), target.getDatasourceId());

        if (!permission.check("tenant-a", "user-1", "PROJECT", project.getProjectId(), "START")) {
            throw new SecurityException("denied");
        }
        AuditService.AuditEvent startEvent = new AuditService.AuditEvent();
        startEvent.setTenantId("tenant-a");
        startEvent.setUserId("user-1");
        startEvent.setAction("START_PIPELINE");
        startEvent.setResourceType("PROJECT");
        startEvent.setResourceId(project.getProjectId());
        startEvent.setSuccess(true);
        audit.record(startEvent);

        // --- 组件层（可拔插） ---
        PluginContainer plugins = new PluginContainer();
        registerDemoPlugins(plugins);

        SchemaMigrator dbCat = new DbCatComponent();
        FullImporter fullImport = new DefaultFullImporter(plugins);
        LogStore store = new StoreComponent();
        IncrApplier incrSync = new IncrSyncComponent();
        Verifier verification = new FullVerificationComponent();
        AiAssistant aiEngine = new AiEngineComponent();

        DefaultSupervisor supervisor = new DefaultSupervisor();
        supervisor.register(dbCat);
        supervisor.register(fullImport);
        supervisor.register(store);
        supervisor.register(incrSync);
        supervisor.register(verification);
        supervisor.register(aiEngine);

        System.out.println("AIEngine suggest: " + aiEngine.suggestMapping(new HashMap<String, String>()));

        // --- 流程编排层 ---
        WorkflowEngine workflow = new WorkflowEngine();
        workflow.register(new ObjectMigrationStage(dbCat));
        workflow.register(new DataMigrationStage(fullImport));
        workflow.register(new DataSyncStage(store, incrSync));
        workflow.register(new DataVerificationStage(verification));
        workflow.register(new LinkSwitchStage());

        Map<String, String> context = new HashMap<String, String>();
        context.put("taskId", "task-" + project.getProjectId());
        context.put("jobId", "job-demo");
        context.put("readerName", "demo-memory-reader");
        context.put("writerName", "demo-memory-writer");
        context.put("rows", "50");
        context.put("maxIncrRecords", "10");
        context.put("cutoverTarget", "target-db");

        List<StageResult> results = workflow.runPipeline(
                context,
                MigrationStage.OBJECT_MIGRATION,
                MigrationStage.DATA_MIGRATION,
                MigrationStage.DATA_SYNC,
                MigrationStage.DATA_VERIFICATION,
                MigrationStage.LINK_SWITCH);

        for (StageResult result : results) {
            System.out.println("Stage => " + result.getMessage() + " (ok=" + result.isSuccess() + ")");
        }

        Communication stats = new Communication();
        stats.addRecords(DemoMemoryWriter.sinkSnapshot().size());
        monitorService.report(context.get("taskId"), stats);

        System.out.println("Project: " + project.getName());
        System.out.println("Writer sink: " + DemoMemoryWriter.sinkSnapshot().size());
        System.out.println("Audit events: " + audit.snapshot().size());
        System.out.println("Supervisor components: " + supervisor.collect().size());
        System.out.println("Monitor tasks: " + monitorService.listTaskIds());
        System.out.println("Crypto decrypt sample ok: "
                + "p".equals(datasourceService.decryptPassword(source.getDatasourceId())));
        System.out.println("=== Diagram-aligned demo finished ===");

        plugins.destroyAll();
    }

    private static void registerDemoPlugins(PluginContainer plugins) {
        PluginDescriptor readerDesc = new PluginDescriptor();
        readerDesc.setName("demo-memory-reader");
        readerDesc.setType(PluginType.READER);
        readerDesc.setClassName(DemoMemoryReader.class.getName());
        readerDesc.setVersion("0.1.0");
        plugins.registerInProcess(readerDesc, new DemoMemoryReader());

        PluginDescriptor writerDesc = new PluginDescriptor();
        writerDesc.setName("demo-memory-writer");
        writerDesc.setType(PluginType.WRITER);
        writerDesc.setClassName(DemoMemoryWriter.class.getName());
        writerDesc.setVersion("0.1.0");
        plugins.registerInProcess(writerDesc, new DemoMemoryWriter());
    }
}
