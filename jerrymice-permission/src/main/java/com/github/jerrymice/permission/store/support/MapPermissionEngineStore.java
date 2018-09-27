package com.github.jerrymice.permission.store.support;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.store.PermissionEngineStore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tumingjian
 * 说明:  普通的map存取器.这是一个线程安全的存取器
 */
public class MapPermissionEngineStore implements PermissionEngineStore<String> {
    private ConcurrentHashMap<String,PermissionEngine> context=new ConcurrentHashMap<>();
    @Override
    public PermissionEngine get(String key) {
        return context.get(key);
    }

    @Override
    public void put(String key, PermissionEngine engine) {
        context.put(key,engine);
    }

    @Override
    public PermissionEngine remove(String key) {
        return context.remove(key);
    }

    @Override
    public boolean contain(String key) {
        return context.contains(key);
    }
}
