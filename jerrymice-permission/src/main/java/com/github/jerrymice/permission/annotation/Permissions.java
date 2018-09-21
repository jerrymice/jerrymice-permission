package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 *
 * 说明:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permissions {
    Permission[] value();
}
