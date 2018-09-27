package com.github.jerrymice.permission.store.support;

import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.resource.Property;
import com.github.jerrymice.permission.store.PermissionEngineStore;
import com.github.jerrymice.permission.store.PermissionServiceAdapter;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * 说明:http session store.  能够从httpsession中获取权限引擎相关的数据
 */
public class HttpSessionPermissionEngineStore implements PermissionEngineStore<HttpSession> {
    private String sessionKey = "__permission_engine_store_data__";
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
        PermissionServiceAdapter attribute = (PermissionServiceAdapter)session.getAttribute(sessionKey);
        if(attribute!=null && session.getAttribute(sessionKey) instanceof PermissionServiceAdapter){
            PermissionEngine engine = generator.defaultPermissionEngine(attribute, config);
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
        PermissionServiceAdapter permissionServiceAdapter = new PermissionServiceAdapter(user.size()>0?user.iterator().next():null, characters, resources, map);
        session.setAttribute(sessionKey, permissionServiceAdapter);
    }

    @Override
    public PermissionEngine remove(HttpSession session) {
        PermissionEngine engine = get(session);
        session.removeAttribute(sessionKey);
        return engine;
    }

    @Override
    public boolean contain(HttpSession session) {
        return session.getAttribute(sessionKey) != null && session.getAttribute(sessionKey) instanceof PermissionServiceAdapter;
    }
}
