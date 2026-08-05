package com.migration.platform.spi.plugin;

import com.migration.platform.spi.record.ColumnRecord;

/**
 * Optional record transformer (masking / filter / convert).
 */
public interface TransformerPlugin extends Plugin {

    ColumnRecord transform(ColumnRecord input);
}
