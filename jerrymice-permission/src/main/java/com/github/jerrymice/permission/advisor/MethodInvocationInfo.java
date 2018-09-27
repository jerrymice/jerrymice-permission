package com.github.jerrymice.permission.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.annotation.PermissionMeta;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.util.AnnotationUtils;
import lombok.Data;
import lombok.Getter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author tumingjian
 * @date 2018/9/27
 * 说明:
 */
@Data
public class MethodInvocationInfo {
    /**
     * 当前方法
     */
    private Method method;
    /**
     * 当前方法的@Permission数组
     */
    private Permission[] permissions;
    /**
     * 当前方法的@PermissionResult数组
     */
    private PermissionResult[] permissionResults;
    /**
     * 当前方法参数
     */
    private Parameter[] parameters;
    /**
     * 方法参数上的注解
     */
    private PermissionMeta[] permissionMetas;
    /**
     * 当前方法参数名
     */
    private String[] parameterNames;
    /**
     * 当前方法的返回值类型
     */
    private Class<?> returnType;

    public MethodInvocationInfo(MethodInvocation methodInvocation) {
        this.method = methodInvocation.getMethod();
        this.permissions = AnnotationUtils.getPermissionAnnotation(method);
        this.permissionResults = AnnotationUtils.getPermissionResultAnnotation(method);
        this.parameters = method.getParameters();
        permissionMetas=new PermissionMeta[parameters.length];
        this.parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
        this.returnType = method.getReturnType();
        for(int i=0;i<parameters.length;i++){
            this.permissionMetas[i] = parameters[i].getAnnotation(PermissionMeta.class);
        }
    }
}
