package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.engine.PermissionEngine;

/**
 * @author tumingjian
 * @date 2018/9/13
 * 说明:
 */
public interface PermissionRejectProcessor {
    /**
     *
     * @param permissionEngine
     * @return
     */
    Object rejectProcess(PermissionEngine permissionEngine);
}
