package com.github.jerrymice.permission.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}
