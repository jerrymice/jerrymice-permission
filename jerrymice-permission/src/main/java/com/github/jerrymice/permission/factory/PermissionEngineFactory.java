package com.github.jerrymice.permission.factory;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.store.PermissionEngineStore;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public interface PermissionEngineFactory {
    PermissionEngine getPermissionEngine();

    PermissionEngine removePermissionEngine();

    PermissionEngineStore getStore();

    PermissionService getLoader();

    PermissionConfig getConfig();

    PermissionRejectProcessor getRejectProcessor();

    PermissionEngineGenerator getGenerator();
    void removeLocalCache();
}
