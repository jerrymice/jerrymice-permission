package com.github.jerrymice.permission.store;

import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.engine.PermissionEngine;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public interface PermissionEngineStore<T> {
    PermissionEngine get(T key);

    void put(T key, PermissionEngine engine);

    PermissionEngine remove(T key);

    boolean contain(T key);
}
