package com.github.jerrymice.permission.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author tumingjian
 *         说明:用于对方法结果的控制和筛选.
 *         该注解会先将方法的原始结果定义为一个js变量(变量名为注解的var属性),
 *         然后根据方法原始结果的值和ignoreNullValue属性值来判断是否执行eval
 *         最后返回一个新的结果(新的结果其实也是一个js变量,要返回哪一个变量名称根据returnVar的属性值来决定)给java
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(PermissionResults.class)
public @interface PermissionResult {
    /**
     * 方法原始结果在Js中的变量名
     *
     * @return String
     */
    String var() default "result";

    /**
     * 要执行的js表达式
     *
     * @return String
     */
    String eval();

    /**
     * 如果方法的原始结果为null时,是否忽略执行eval属性中的表达式.
     *
     * @return boolean
     */
    boolean ignoreNullValue() default true;

    /**
     * 最终要返回给java的js变量名.如果不指定默认返回注解var属性中的变量名的JS变量的值.
     *
     * @return String
     */
    String returnVar() default "";
}
