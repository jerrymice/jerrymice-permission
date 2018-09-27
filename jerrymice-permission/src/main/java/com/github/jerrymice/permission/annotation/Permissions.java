package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明:一个在同一方法内可以支持多个Permission注解的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permissions {
    Permission[] value();
}
