package com.migration.platform.core.plugin;

import com.migration.platform.spi.plugin.Plugin;
import com.migration.platform.spi.plugin.PluginContext;
import com.migration.platform.spi.plugin.PluginDescriptor;
import com.migration.platform.spi.plugin.PluginType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Scans plugin directories, isolates ClassLoaders, loads plugin.json entries.
 */
public class PluginContainer {

    private final Map<String, LoadedPlugin> plugins = new ConcurrentHashMap<String, LoadedPlugin>();

    public void loadDirectory(File root) throws Exception {
        if (root == null || !root.isDirectory()) {
            throw new IllegalArgumentException("plugin root not found: " + root);
        }
        File[] children = root.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                File json = new File(child, "plugin.json");
                if (json.isFile()) {
                    loadPluginDir(child, json);
                }
            }
        }
    }

    public void registerInProcess(PluginDescriptor descriptor, Plugin plugin) {
        plugin.init(new PluginContext(descriptor.getProperties()));
        plugins.put(descriptor.key(), new LoadedPlugin(descriptor, plugin, null));
    }

    @SuppressWarnings("unchecked")
    public <T extends Plugin> T get(PluginType type, String name, Class<T> expected) {
        LoadedPlugin loaded = plugins.get(type.name() + ":" + name);
        if (loaded == null) {
            throw new IllegalArgumentException("plugin not found: " + type + ":" + name);
        }
        if (!expected.isInstance(loaded.plugin)) {
            throw new IllegalStateException("plugin type mismatch for " + loaded.descriptor.key());
        }
        return (T) loaded.plugin;
    }

    public List<PluginDescriptor> list() {
        List<PluginDescriptor> list = new ArrayList<PluginDescriptor>();
        for (LoadedPlugin loaded : plugins.values()) {
            list.add(loaded.descriptor);
        }
        return list;
    }

    public void destroyAll() {
        for (LoadedPlugin loaded : plugins.values()) {
            try {
                loaded.plugin.destroy();
            } catch (Exception ignored) {
                // best effort
            }
            if (loaded.classLoader != null) {
                try {
                    loaded.classLoader.close();
                } catch (IOException ignored) {
                    // best effort
                }
            }
        }
        plugins.clear();
    }

    private void loadPluginDir(File dir, File jsonFile) throws Exception {
        PluginDescriptor descriptor = PluginJsonParser.parse(readFile(jsonFile));
        List<URL> urls = new ArrayList<URL>();
        File[] jars = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File d, String name) {
                return name.endsWith(".jar");
            }
        });
        if (jars != null) {
            for (File jar : jars) {
                urls.add(jar.toURI().toURL());
            }
        }
        // Also include classes/ for exploded plugins in demos/tests.
        File classes = new File(dir, "classes");
        if (classes.isDirectory()) {
            urls.add(classes.toURI().toURL());
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]), PluginContainer.class.getClassLoader());
        Class<?> clazz = Class.forName(descriptor.getClassName(), true, loader);
        Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
        Map<String, String> cfg = new HashMap<String, String>(descriptor.getProperties());
        cfg.put("plugin.home", dir.getAbsolutePath());
        plugin.init(new PluginContext(cfg));
        plugins.put(descriptor.key(), new LoadedPlugin(descriptor, plugin, loader));
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream in = new java.io.FileInputStream(file);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            reader.close();
        } finally {
            in.close();
        }
        return sb.toString();
    }

    private static final class LoadedPlugin {
        private final PluginDescriptor descriptor;
        private final Plugin plugin;
        private final URLClassLoader classLoader;

        private LoadedPlugin(PluginDescriptor descriptor, Plugin plugin, URLClassLoader classLoader) {
            this.descriptor = descriptor;
            this.plugin = plugin;
            this.classLoader = classLoader;
        }
    }
}
