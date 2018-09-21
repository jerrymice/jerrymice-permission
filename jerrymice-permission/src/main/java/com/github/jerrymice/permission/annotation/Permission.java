package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 *
 * 说明:
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
@Documented
public @interface Permission {
    /**
     * 支持两种值
     * 可以是资源code或者name
     * 也可以是一个返回true或false的表达式
     * @return 返回一个true或false
     */
    String value();

    String remark() default "";
}
