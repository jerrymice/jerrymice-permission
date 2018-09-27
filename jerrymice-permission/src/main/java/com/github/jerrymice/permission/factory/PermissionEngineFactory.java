package com.github.jerrymice.permission.factory;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.store.PermissionEngineStore;

/**
 * @author tumingjian
 * 说明:权限脚本引擎工厂类.负责缓存,从store中存取,移除脚本引擎
 */
public interface PermissionEngineFactory {
    /**
     * 从store中获取当前用户的权限控制引擎
     * @return PermissionEngine
     */
    PermissionEngine getPermissionEngine();

    /**
     * 从store中移除当前用户的权限控制引擎
     * 如果 store为true,那么将从ThreadLocal和store中移除权限控制引擎,如果为false,那么仅仅只是移除ThreadLocal中缓存的权限控制引擎
     * @param store  true | false
     */
    void removePermissionEngine(boolean store);

    /**
     * 获取权限引擎存取仓库
     * @return PermissionEngineStore
     */
    PermissionEngineStore getStore();

    /**
     * 获取用户权限相关的信息
     * @return PermissionService
     */
    PermissionService getPermissionService();

    /**
     * 获取权限全局配置信息
     * @return PermissionConfig
     */

    PermissionConfig getConfig();

    /**
     * 获取权限不足时拒绝处理者
     * @return   PermissionRejectProcessor
     */
    PermissionRejectProcessor getRejectProcessor();

    /**
     * 获取权限引擎生成者
     * @return PermissionEngineGenerator
     */
    PermissionEngineGenerator getGenerator();
}
