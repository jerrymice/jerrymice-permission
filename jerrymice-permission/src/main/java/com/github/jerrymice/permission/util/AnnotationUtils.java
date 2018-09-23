package com.github.jerrymice.permission.util;

import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.annotation.PermissionResults;
import com.github.jerrymice.permission.annotation.Permissions;

import java.lang.reflect.Method;

/**
 * @author tumingjian
 * @date 2018/9/23
 * 说明:
 */
public class AnnotationUtils {
    /**
     * 获取Permission注解
     * @param method 要获取注解的方法
     * @return 返回值
     */
    public static Permission[] getPermissionAnnotation(Method method) {
        Permission[] permissions = null;
        Permissions annotation = method.getAnnotation(Permissions.class);
        if (annotation != null) {
            permissions = annotation.value();
        } else {
            Permission permissionAnnotation = method.getAnnotation(Permission.class);
            if (permissionAnnotation != null) {
                permissions = new Permission[]{permissionAnnotation};
            }
        }
        return permissions;
    }


    /**
     * 获取方法上的PermissionResult注解
     * @param method 要获取注解的方法
     * @return  返回值
     */
    public static PermissionResult[] getPermissionResultAnnotation(Method method) {
        PermissionResult[] results = null;
        PermissionResults annotation = method.getAnnotation(PermissionResults.class);
        if (annotation != null) {
            results = annotation.value();
        } else {
            PermissionResult permissionAnnotation = method.getAnnotation(PermissionResult.class);
            if (permissionAnnotation != null) {
                results = new PermissionResult[]{permissionAnnotation};
            }
        }
        return results;
    }
}
