package com.github.jerrymice.permission.annotation;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明: 权限判定注解.用于判定当前用户是否有权限访问有该注解修饰的方法,value值支持普通文本和js表达式
 * 如果是普通文本,那么将调用PermissionEngine.contain()方法来判断
 * 如果是js表达式,那么将调用PermissionEngine.bool()方法来判断
 *
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

    /**
     * 注释内容
     * @return 注释
     */
    String remark() default "";
}
