package com.github.jerrymice.permission.spring;

import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.annotation.Permissions;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author tumingjian
 *         <p>
 *         说明:权限控制的切面,这个切面的优先级应该是所有切面中最高的.
 */
@Aspect
public class PermissionEngineAspect {
    private PermissionEngineFactory factory;

    public PermissionEngineAspect(PermissionEngineFactory factory) {
        this.factory = factory;
    }

    public PermissionEngineFactory getFactory() {
        return factory;
    }

    public void setFactory(PermissionEngineFactory factory) {
        this.factory = factory;
    }

    public PermissionEngineAspect() {

    }

    @Pointcut(value = "@annotation(com.github.jerrymice.permission.annotation.Permissions)")
    public void pointCuts() {

    }

    @Pointcut(value = "@annotation(com.github.jerrymice.permission.annotation.Permission)")
    public void pointCut() {

    }

    /**
     * aop注入.权限控制器
     *
     * @param joinPoint 切入点
     * @return 返回函数的执行结果
     * @throws Throwable 异常信息
     */
    @Around(value = "pointCut() && @annotation(permission)")
    public Object round(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        return roundProcess(joinPoint, new Permission[]{permission});
    }

    /**
     * aop注入.权限控制器
     *
     * @param joinPoint 切入点
     * @return 返回函数的执行结果
     * @throws Throwable 异常信息
     */
    @Around(value = "pointCuts() && @annotation(permissions)")
    public Object around(ProceedingJoinPoint joinPoint, Permissions permissions) throws Throwable {
        Permission[] list = permissions.value();
        return roundProcess(joinPoint, list);
    }

    /**
     * 切入运行函数
     *
     * @param joinPoint 切入点
     * @param list      注解列表
     * @return 函数返回值
     * @throws Throwable 异常信息
     */
    private Object roundProcess(ProceedingJoinPoint joinPoint, Permission[] list) throws Throwable {
        PermissionEngine engine = factory.getPermissionEngine();
        boolean isPermission = false;
        for (Permission per : list) {
            String value = per.value();
            int type = engine.type(value);
            if (type == 1) {
                boolean bool = engine.bool(value);
                isPermission = bool;
            } else {
                boolean bool = engine.contain(value);
                isPermission = bool;
            }
            if (isPermission) {
                break;
            }
        }
        if (!isPermission) {
            Object o = factory.getRejectProcessor().rejectProcess(engine);
            return o;
        } else {
            if (joinPoint.getArgs() == null || joinPoint.getArgs().length == 0) {
                Object proceed = joinPoint.proceed();
                return proceed;
            } else {
                Object proceed = joinPoint.proceed(joinPoint.getArgs());
                return proceed;
            }
        }
    }

}
