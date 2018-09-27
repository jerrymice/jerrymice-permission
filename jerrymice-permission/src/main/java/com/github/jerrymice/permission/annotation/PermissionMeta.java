package com.github.jerrymice.permission.annotation;


import java.lang.annotation.*;

/**
 * @author tumingjian
 * 说明:用于对方法参数的检查和筛选.
 *
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionMeta {
    /**
     * 参数在JS引擎中的变量名,为空时,将使用方法的参数名作为JS变量名.这个变量名将在同一个方法调用结束后释放
     * @return String
     */
    String var() default "";
    /**
     * 如果参数传入的值为空时,可以用defaultValue指定一个默认值,这个值必须是JS支持的eval表达式
     * @return String
     */
    String defaultValue() default "";

    /**
     * 如果传入参数的值不为空时,可以对参数值进行进一步的处理.这个会在defaultValue之后执行.
     * 也就是如果传入参数为空时,会对默认值进行处理
     * @return String
     */
    String eval() default "";

    /**
     * 最终要返回给java的js变量名.如果不指定默认返回注解var属性中的变量名的JS变量的值.
     *
     * @return String
     */
    String returnVar() default "";
}
