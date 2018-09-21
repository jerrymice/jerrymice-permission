package com.github.jerrymice.permission.factory.support;

import com.github.jerrymice.permission.listener.PermissionEngineRequestContextHolder;
import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.store.support.HttpSessionPermissionEngineStore;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public class WebPermissionEngineFactory extends PermissionEngineFactorySupport {

    public WebPermissionEngineFactory() {
        initDefaultConfig();
    }
    @Override
    protected void initDefaultConfig(){
        super.initDefaultConfig();
        this.setStore(new HttpSessionPermissionEngineStore(this.getGenerator(),this.getConfig()));
        this.setGenerator(new PermissionEngineGenerator());
    }
    public class PermissionEngineGenerator implements com.github.jerrymice.permission.config.PermissionEngineGenerator {
        @Override
        public Object getKey(PermissionLoader permissionLoader) {
            return PermissionEngineRequestContextHolder.getSession();
        }
    }
}
