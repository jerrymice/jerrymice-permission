package com.github.jerrymice.permission.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@Data
@ConfigurationProperties(prefix = "spring.jerrymice.permission")
@Configuration
public class JerryMicePermissionProperties {
    private boolean enabled = true;
    private boolean mixtureSearch = false;
    private String usernameSessionKey = "currentUsername";
}
