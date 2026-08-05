package com.migration.platform.spi.plugin;

/**
 * Schema / type mapping plugin.
 */
public interface TypeMapperPlugin extends Plugin {

    String mapType(String sourceDb, String targetDb, String sourceType);

    String rewriteDdl(String sourceDb, String targetDb, String sourceDdl);
}
