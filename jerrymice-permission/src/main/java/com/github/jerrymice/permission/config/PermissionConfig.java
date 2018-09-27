package com.github.jerrymice.permission.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tumingjian
 * 说明:权限控制全局配置文件
 */
@Data
@Accessors(chain = true)
public class PermissionConfig {
    /**
     * 是否全局匹配
     * @See PermissionEngine#contain方法
     */
    private boolean mixtureSearch = false;
    /**
     * 是否在threadLocal中缓存当前线程的PermissionEngine引擎
     * @See PermissionEngineFactorySupport#getPermissionEngine方法
     */
    private boolean threadLocalCache = true;
}
