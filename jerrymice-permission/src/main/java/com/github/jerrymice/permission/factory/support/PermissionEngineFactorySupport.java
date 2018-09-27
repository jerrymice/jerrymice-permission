package com.github.jerrymice.permission.factory.support;

import com.github.jerrymice.common.entity.entity.ResultInfo;
import com.github.jerrymice.permission.config.*;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import com.github.jerrymice.permission.resource.Property;
import com.github.jerrymice.permission.store.PermissionEngineStore;
import com.github.jerrymice.permission.store.support.MapPermissionEngineStore;

import java.text.MessageFormat;
import java.util.Set;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public class PermissionEngineFactorySupport implements PermissionEngineFactory {

    private PermissionService loader;
    private PermissionConfig config;
    private PermissionRejectProcessor rejectProcessor;
    private PermissionEngineGenerator generator;
    private PermissionEngineStore store;
    private ThreadLocal<PermissionEngine> localEngine = new ThreadLocal<>();

    public PermissionEngineFactorySupport() {
        initDefaultConfig();
    }

    public class UsernameGenerator implements PermissionEngineGenerator {
        @Override
        public Object getKey(PermissionService permissionLoader) {
            return permissionLoader.loadUser();
        }
    }

    public class DefaultPermissionRejectProcessor implements PermissionRejectProcessor {
        @Override
        public Object rejectProcess(PermissionEngine permissionEngine) {
            Set<Property> user = permissionEngine.user();
            if (user != null && user.size() > 0) {
                return new ResultInfo<>(false).setMessage(MessageFormat.format("当前用户:{0},没有访问该接口的权限", permissionEngine.user()));
            } else {
                return new ResultInfo<>(false).setMessage(MessageFormat.format("当前用户:{0},没有访问该接口的权限", "anonymous"));
            }
        }
    }

    protected void initDefaultConfig() {
        this.config = new PermissionConfig().setMixtureSearch(true);
        rejectProcessor = new DefaultPermissionRejectProcessor();
        generator = new UsernameGenerator();
        store = new MapPermissionEngineStore();
    }

    @Override
    public PermissionEngine getPermissionEngine() {
        if (config.isThreadLocalCache()) {
            PermissionEngine engine = localEngine.get();
            if (engine != null) {
                return engine;
            }
        }
        Object key = generator.getKey(loader);
        PermissionEngine cacheEngine = store.get(key);
        if (cacheEngine != null) {
            return cacheThreadLocal(cacheEngine);
        } else {
            PermissionEngine permissionEngine = generator.defaultPermissionEngine(loader, config);
            store.put(key, permissionEngine);
            return cacheThreadLocal(permissionEngine);
        }
    }

    private PermissionEngine cacheThreadLocal(PermissionEngine engine) {
        if (config.isThreadLocalCache()) {
            localEngine.set(engine);
        }
        return engine;
    }

    @Override
    public void removePermissionEngine(boolean store) {
        removeLocalCache();
        if(store){
            this.store.remove(generator.getKey(loader));
        }
    }

    private void removeLocalCache() {
        if (config.isThreadLocalCache()) {
            this.localEngine.remove();
        }
    }

    @Override
    public PermissionEngineStore getStore() {
        return store;
    }

    public void setStore(PermissionEngineStore store) {
        this.store = store;
    }

    @Override
    public PermissionService getLoader() {
        return loader;
    }

    public void setLoader(PermissionService loader) {
        this.loader = loader;
    }

    @Override
    public PermissionConfig getConfig() {
        return config;
    }

    public void setConfig(PermissionConfig config) {
        this.config = config;
    }

    @Override
    public PermissionRejectProcessor getRejectProcessor() {
        return rejectProcessor;
    }

    public void setRejectProcessor(PermissionRejectProcessor rejectProcessor) {
        this.rejectProcessor = rejectProcessor;
    }

    @Override
    public PermissionEngineGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(PermissionEngineGenerator generator) {
        this.generator = generator;
    }
}
