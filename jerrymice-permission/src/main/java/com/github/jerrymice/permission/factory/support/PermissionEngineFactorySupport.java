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
 * 说明: 权限脚本引擎工厂类.负责缓存,从store中存取,移除脚本引擎
 */
public class PermissionEngineFactorySupport implements PermissionEngineFactory {
    /**
     * 初始化用户权限相关信息的类
     */
    private PermissionService permissionService;
    /**
     * 权限管理全局配置类
     */
    private PermissionConfig config;
    /**
     * 权限不足时的返回信息处理者
     */
    private PermissionRejectProcessor rejectProcessor;
    /**
     * 权限脚本引擎生成者
     */
    private PermissionEngineGenerator generator;
    /**
     * 权限脚本引擎存取仓库
     */
    private PermissionEngineStore store;
    /**
     * 权限脚本引擎的线程缓存存取变量
     */
    private ThreadLocal<PermissionEngine> localEngine = new ThreadLocal<>();

    public PermissionEngineFactorySupport() {
        initDefaultConfig();
    }

    /**
     * 默认的store key生成器.直接以用户名作为key
     */
    public class UsernameGenerator implements PermissionEngineGenerator {
        @Override
        public Object getKey(PermissionService permissionLoader) {
            return permissionLoader.loadUser();
        }
    }

    /**
     * 默认的权限不足时的返回信息处理器.
     */
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

    /**
     * 如果没有指定factory的一些属性配置,那么初始化一份默认的属性配置
     */
    protected void initDefaultConfig() {
        this.config = new PermissionConfig().setMixtureSearch(false).setThreadLocalCache(true);
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
        Object key = generator.getKey(permissionService);
        PermissionEngine cacheEngine = store.get(key);
        if (cacheEngine != null) {
            return cacheThreadLocal(cacheEngine);
        } else {
            PermissionEngine permissionEngine = generator.defaultPermissionEngine(permissionService, config);
            store.put(key, permissionEngine);
            return cacheThreadLocal(permissionEngine);
        }
    }

    /**
     * 缓存当前引擎到ThreadLocal中
     * @param engine
     * @return
     */
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
            this.store.remove(generator.getKey(permissionService));
        }
    }

    /**
     * 移除ThreadLocal中的当前引擎
     */
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

    public PermissionService getPermissionService() {
        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
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
