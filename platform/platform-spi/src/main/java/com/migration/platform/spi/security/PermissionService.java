package com.migration.platform.spi.security;

/**
 * Authorization SPI (Permission module).
 */
public interface PermissionService {

    boolean check(String tenantId, String userId, String resourceType, String resourceId, String action);
}
