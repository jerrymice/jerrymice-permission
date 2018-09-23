package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * @date 2018/9/23
 * 说明:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionResults {
    PermissionResult[] value();
}
