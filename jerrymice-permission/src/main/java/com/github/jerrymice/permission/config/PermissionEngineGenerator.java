package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.support.GoogleV8PermissionEngine;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
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
