package com.github.jerrymice.permission.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明:
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionMeta {
    /**
     * 参数在JS引擎中的变量名,为空时,将用方法的参数名作为JS变量名.这个变量名将在同一个方法调用结束后释放
     * @return
     */
    String var() default "";
    /**
     * 如果参数传入的值为空时,可以用defaultValue指定一个默认值,只支持eval表达式取值.
     * @return
     */
    String defaultValue() default "";

    /**
     * 如果传入参数的值不为空时,可以对参数值进行进一步的处理.这个会在defaultValue之后执行.
     * 也就是如果传入参数为空时,会对默认值进行处理
     * @return
     */
    String eval() default "";
}
