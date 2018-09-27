package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.engine.PermissionEngine;

/**
 * @author tumingjian
 * 说明:权限不足时的拒绝处理方法
 */
@FunctionalInterface
public interface PermissionRejectProcessor {
    /**
     *
     * @param permissionEngine  当前engine
     * @return  如果权限不足时,该方法的返回值.
     */
    Object rejectProcess(PermissionEngine permissionEngine);
}
