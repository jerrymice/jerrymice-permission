package com.github.jerrymice.permission.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.util.List;

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
    /**
     * 扩展脚本文件.会在创建引擎之后立即加载这些脚本文件
     */
    private List<Resource> extendScriptFile;
}
