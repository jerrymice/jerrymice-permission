package com.github.jerrymice.permission.store.support;

import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import com.github.jerrymice.permission.resource.Property;
import com.github.jerrymice.permission.store.PermissionEngineStore;
import com.github.jerrymice.permission.store.PermissionStoreData;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public class HttpSessionPermissionEngineStore implements PermissionEngineStore<HttpSession> {
    private String sessionKey = "__permission_engine_store_data__";
    private static ThreadLocal<PermissionEngine> CURRENT_THREAD_ENGINE=new ThreadLocal<>();
    private PermissionEngineGenerator generator;
    private PermissionConfig config;

    public HttpSessionPermissionEngineStore(String sessionKey, PermissionEngineGenerator generator, PermissionConfig config) {
        this.sessionKey = sessionKey;
        this.generator = generator;
        this.config = config;
    }

    public HttpSessionPermissionEngineStore(PermissionEngineGenerator generator, PermissionConfig config) {
        this.generator = generator;
        this.config = config;
    }

    @Override
    public PermissionEngine get(HttpSession session) {
        if(CURRENT_THREAD_ENGINE.get()!=null){
            return CURRENT_THREAD_ENGINE.get();
        }
        PermissionStoreData attribute = (PermissionStoreData)session.getAttribute(sessionKey);
        if(attribute!=null && session.getAttribute(sessionKey) instanceof PermissionStoreData){
            PermissionEngine engine = generator.defaultPermissionEngine(attribute, config);
            CURRENT_THREAD_ENGINE.set(engine);
            return engine;
        }
        return null;
    }

    @Override
    public void put(HttpSession session, PermissionEngine engine) {
        Set<Property> user = engine.user();
        Set<Property> characters = engine.characters();
        Set<Property> resources = engine.resources();
        Map<String, Object> map = engine.extendData();
        PermissionStoreData permissionStoreData = new PermissionStoreData(user.size()>0?user.iterator().next():null, characters, resources, map);
        session.setAttribute(sessionKey, permissionStoreData);
        CURRENT_THREAD_ENGINE.set(engine);
    }

    @Override
    public PermissionEngine remove(HttpSession session) {
        PermissionEngine engine = get(session);
        session.removeAttribute(sessionKey);
        CURRENT_THREAD_ENGINE.remove();
        return engine;
    }

    @Override
    public boolean contain(HttpSession session) {
        return session.getAttribute(sessionKey) != null && session.getAttribute(sessionKey) instanceof PermissionStoreData;
    }
}
