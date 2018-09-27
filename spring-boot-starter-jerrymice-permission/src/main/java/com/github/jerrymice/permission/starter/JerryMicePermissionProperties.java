package com.github.jerrymice.permission.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author tumingjian
 * 说明:  权限控制的全局配置
 */
@Data
@ConfigurationProperties(prefix = "spring.jerrymice.permission")
@Configuration
public class JerryMicePermissionProperties {
    /**
     * 是否启用复合的匹配.
     */
    private boolean mixtureSearch = false;
    /**
     * 是否将权限控制引擎缓存到ThreadLocal中
     */
    private boolean threadLocalCache = true;
    /**
     * 扩展脚本文件.会在创建引擎之后立即加载这些脚本文件
     */
    private List<Resource> extendScriptFile;
}
