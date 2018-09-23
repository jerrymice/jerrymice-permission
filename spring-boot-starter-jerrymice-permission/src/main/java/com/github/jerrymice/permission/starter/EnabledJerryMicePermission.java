package com.github.jerrymice.permission.starter;



import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties(JerryMicePermissionProperties.class)
@Import({PermissionEngineAutoConfiguration.class})
public @interface EnabledJerryMicePermission {

}
