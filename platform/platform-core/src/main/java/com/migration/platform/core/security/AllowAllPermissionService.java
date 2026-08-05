package com.migration.platform.core.security;

import com.migration.platform.spi.security.PermissionService;

/**
 * Allow-all placeholder for local demo.
 */
public class AllowAllPermissionService implements PermissionService {

    @Override
    public boolean check(String tenantId, String userId, String resourceType, String resourceId, String action) {
        return true;
    }
}
