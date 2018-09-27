package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.support.GoogleV8PermissionEngine;

/**
 * @author tumingjian
 * 说明:  新的PermissionEngine创建者.创建出来的的Permission会被存入到PermissionStore中
 * @See PermissionStore
 *
 */
public interface PermissionEngineGenerator {
    /**
     * 引擎中的key生成策略
     * @param permissionLoader 基本参数
     * @return 返回一个唯一的key
     */
    Object getKey(PermissionService permissionLoader);

    /**
     * 创建引擎.如果要创建其他脚本语言的引擎可以自己实现这个接口
     * @param permissionLoader  基本参数
     * @param config   全局配置
     * @return  返回一个引擎
     */
    default PermissionEngine defaultPermissionEngine(PermissionService permissionLoader, PermissionConfig config) {
        return new GoogleV8PermissionEngine(permissionLoader, config.isMixtureSearch());
    }
}
