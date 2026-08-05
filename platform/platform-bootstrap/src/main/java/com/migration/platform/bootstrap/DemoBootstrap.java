package com.migration.platform.bootstrap;

import com.migration.platform.component.FullImporter;
import com.migration.platform.control.meta.InMemoryMetaStore;
import com.migration.platform.control.node.InMemoryNodeRegistry;
import com.migration.platform.control.node.NodeInfo;
import com.migration.platform.control.orchestrator.Orchestrator;
import com.migration.platform.control.schedule.JobDispatcher;
import com.migration.platform.control.supervisor.DefaultSupervisor;
import com.migration.platform.core.config.JobConfig;
import com.migration.platform.core.plugin.PluginContainer;
import com.migration.platform.core.security.AllowAllPermissionService;
import com.migration.platform.core.security.DemoCryptoService;
import com.migration.platform.core.security.InMemoryAuditService;
import com.migration.platform.core.stats.Communication;
import com.migration.platform.plugins.demo.DemoMemoryReader;
import com.migration.platform.plugins.demo.DemoMemoryWriter;
import com.migration.platform.runtime.component.DefaultFullImporter;
import com.migration.platform.spi.plugin.PluginDescriptor;
import com.migration.platform.spi.plugin.PluginType;
import com.migration.platform.spi.security.CryptoService;

import java.util.HashMap;
import java.util.Map;

/**
 * Local end-to-end demo:
 * register plugins -> control plane dispatch -> SyncWorker full pipeline.
 */
public final class DemoBootstrap {

    private DemoBootstrap() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Migration Platform Demo (Java 8) ===");

        PluginContainer plugins = new PluginContainer();
        registerDemoPlugins(plugins);

        CryptoService crypto = new DemoCryptoService();
        String secret = crypto.encrypt("demo-password", "default");
        System.out.println("Crypto roundtrip ok: " + "demo-password".equals(crypto.decrypt(secret, "default")));

        InMemoryAuditService audit = new InMemoryAuditService();
        InMemoryNodeRegistry registry = new InMemoryNodeRegistry();
        NodeInfo local = new NodeInfo();
        local.setNodeId("node-local");
        local.setHost("127.0.0.1");
        local.setPort(8088);
        local.setRole("worker");
        registry.register(local);

        JobDispatcher dispatcher = new JobDispatcher(registry);
        InMemoryMetaStore metaStore = new InMemoryMetaStore();
        Orchestrator orchestrator = new Orchestrator(
                new AllowAllPermissionService(), audit, metaStore, dispatcher);

        JobConfig config = new JobConfig();
        config.setTaskId("task-demo-001");
        config.setReaderName("demo-memory-reader");
        config.setWriterName("demo-memory-writer");
        Map<String, String> readerParams = new HashMap<String, String>();
        readerParams.put("rows", "50");
        config.setReaderParams(readerParams);

        String dispatchId = orchestrator.createAndStartFullJob("tenant-a", "user-1", config);
        System.out.println("Dispatched job: " + dispatchId);

        JobDispatcher.DispatchedJob job = dispatcher.poll();
        if (job == null) {
            throw new IllegalStateException("no job dispatched");
        }

        DefaultSupervisor supervisor = new DefaultSupervisor();
        FullImporter fullImporter = new DefaultFullImporter(plugins);
        supervisor.register(fullImporter);

        Communication communication = fullImporter.runFullImport(job.getConfig());
        System.out.println("FullImport stats: " + communication);
        System.out.println("Writer sink size: " + DemoMemoryWriter.sinkSnapshot().size());
        System.out.println("Audit events: " + audit.snapshot().size());
        System.out.println("Supervisor heartbeats: " + supervisor.collect().size());
        System.out.println("Meta position: " + metaStore.getPosition(config.getTaskId()));
        System.out.println("=== Demo finished successfully ===");

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
