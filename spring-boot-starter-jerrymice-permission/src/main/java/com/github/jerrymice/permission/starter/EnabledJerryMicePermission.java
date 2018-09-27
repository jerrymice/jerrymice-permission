package com.github.jerrymice.permission.starter;



import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明:在spring boot 环境下要启用权限控制引擎的注解开关
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties(JerryMicePermissionProperties.class)
@Import({PermissionEngineAutoConfiguration.class})
public @interface EnabledJerryMicePermission {

}
