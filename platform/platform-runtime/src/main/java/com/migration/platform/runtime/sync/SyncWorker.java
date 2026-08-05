package com.migration.platform.runtime.sync;

import com.migration.platform.core.channel.Channel;
import com.migration.platform.core.channel.MemoryChannel;
import com.migration.platform.core.config.JobConfig;
import com.migration.platform.core.plugin.PluginContainer;
import com.migration.platform.core.stats.Communication;
import com.migration.platform.spi.plugin.PluginContext;
import com.migration.platform.spi.plugin.PluginType;
import com.migration.platform.spi.plugin.ReaderPlugin;
import com.migration.platform.spi.plugin.WriterPlugin;
import com.migration.platform.spi.record.ColumnRecord;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Full-load runtime: Reader -> Channel -> Writer (Writer starts first).
 */
public class SyncWorker {

    private final PluginContainer pluginContainer;

    public SyncWorker(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    public Communication execute(JobConfig config) throws Exception {
        ReaderPlugin readerPlugin = pluginContainer.get(PluginType.READER, config.getReaderName(), ReaderPlugin.class);
        WriterPlugin writerPlugin = pluginContainer.get(PluginType.WRITER, config.getWriterName(), WriterPlugin.class);

        ReaderPlugin.Job readerJob = readerPlugin.createJob();
        WriterPlugin.Job writerJob = writerPlugin.createJob();
        readerJob.init(new PluginContext(config.readerContext()));
        writerJob.init(new PluginContext(config.writerContext()));

        final Channel channel = new MemoryChannel(config.getChannelCapacity(), config.getChannelMaxBytes());
        final Communication communication = new Communication();
        final AtomicReference<Exception> error = new AtomicReference<Exception>();
        final CountDownLatch writerStarted = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> writerFuture = pool.submit(new Runnable() {
                @Override
                public void run() {
                    WriterPlugin.Task task = writerPlugin.createTask();
                    try {
                        task.init(new PluginContext(config.writerContext()));
                        writerStarted.countDown();
                        task.startWrite(new WriterPlugin.RecordReceiver() {
                            @Override
                            public ColumnRecord getFromReader() {
                                try {
                                    ColumnRecord record = channel.pull();
                                    if (record != null) {
                                        communication.addRecords(1);
                                        communication.addBytes(64L * Math.max(1, record.size()));
                                    }
                                    return record;
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return null;
                                }
                            }
                        });
                    } catch (Exception e) {
                        error.compareAndSet(null, e);
                    } finally {
                        try {
                            task.destroy();
                        } catch (Exception ignored) {
                        }
                        channel.close();
                    }
                }
            });

            writerStarted.await();

            Future<?> readerFuture = pool.submit(new Runnable() {
                @Override
                public void run() {
                    ReaderPlugin.Task task = readerPlugin.createTask();
                    try {
                        task.init(new PluginContext(config.readerContext()));
                        task.startRead(new ReaderPlugin.RecordSender() {
                            @Override
                            public void send(ColumnRecord record) {
                                try {
                                    channel.push(record);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException(e);
                                }
                            }

                            @Override
                            public void terminate() {
                                channel.close();
                            }
                        });
                    } catch (Exception e) {
                        error.compareAndSet(null, e);
                        channel.close();
                    } finally {
                        try {
                            task.destroy();
                        } catch (Exception ignored) {
                        }
                        channel.close();
                    }
                }
            });

            readerFuture.get();
            writerFuture.get();
            if (error.get() != null) {
                throw error.get();
            }
            return communication;
        } finally {
            pool.shutdownNow();
            readerJob.destroy();
            writerJob.destroy();
        }
    }
}
