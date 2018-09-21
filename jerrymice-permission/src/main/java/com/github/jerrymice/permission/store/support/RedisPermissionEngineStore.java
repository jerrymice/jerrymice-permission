package com.github.jerrymice.permission.store.support;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.store.PermissionEngineStore;

/**
 * @author tumingjian
 * @date 2018/9/20
 * 说明:
 */
public class RedisPermissionEngineStore implements PermissionEngineStore<String> {
    @Override
    public PermissionEngine get(String key) {
        return null;
    }

    @Override
    public void put(String key, PermissionEngine engine) {

    }

    @Override
    public PermissionEngine remove(String key) {
        return null;
    }

    @Override
    public boolean contain(String key) {
        return false;
    }
}
