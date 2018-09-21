package com.github.jerrymice.permission.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * @date 2018/9/18
 * 说明:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionResult {
    String var() default "result";
    String eval();
    String returnVar() default "result";
    boolean ignoreNullValue() default true;
}
