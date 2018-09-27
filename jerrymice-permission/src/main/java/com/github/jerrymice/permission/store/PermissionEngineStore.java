package com.github.jerrymice.permission.store;

import com.github.jerrymice.permission.engine.PermissionEngine;

/**
 * @author tumingjian
 * 说明:权限引擎存取仓库抽象接口
 */
public interface PermissionEngineStore<T> {
    /**
     * 根据key在仓库中获取一个PermissionEngine
     * @param key  名称
     * @return PermissionEngine
     */
    PermissionEngine get(T key);
    /**
     * 放入一个PermissionEngine在仓库中
     * @param key 名称
     * @return PermissionEngine
     */
    void put(T key, PermissionEngine engine);
    /**
     * 根据key在仓库中移除一个PermissionEngine
     * @param key 名称
     * @return PermissionEngine
     */
    PermissionEngine remove(T key);
    /**
     * 根据key判断一个PermissionEngine是否存在
     * @param key 名称
     * @return PermissionEngine
     */
    boolean contain(T key);
}
