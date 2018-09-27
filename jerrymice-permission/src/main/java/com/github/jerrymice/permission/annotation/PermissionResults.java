package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明: 一个支持在同一方法上可多次使用PermissionResult注解的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionResults {
    PermissionResult[] value();
}
