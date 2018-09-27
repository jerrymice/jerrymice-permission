package com.github.jerrymice.permission.factory.support;

import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.listener.PermissionEngineRequestContextHolder;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.store.support.HttpSessionPermissionEngineStore;

/**
 * @author tumingjian
 * 说明: 权限脚本引擎工厂类.负责缓存,从store中存取,移除脚本引擎
 * 该类合适在web环境下使用,会将当前用户权限相关的信息直接存入到当前用户的session中.每次存取都会从session中读取或写入数据.
 */
public class WebPermissionEngineFactory extends PermissionEngineFactorySupport {

    public WebPermissionEngineFactory() {
        initDefaultConfig();
    }
    @Override
    protected void initDefaultConfig(){
        super.initDefaultConfig();
        this.setStore(new HttpSessionPermissionEngineStore(this.getGenerator(),this.getConfig()));
        this.setGenerator(new HttpSessionPermissionEngineGenerator());
    }

    /**
     * 以当前用户session为key
     */
    public class HttpSessionPermissionEngineGenerator implements PermissionEngineGenerator {
        @Override
        public Object getKey(PermissionService permissionLoader) {
            return PermissionEngineRequestContextHolder.getSession();
        }
    }
}
