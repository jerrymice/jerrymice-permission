package com.github.jerrymice.permission.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.annotation.PermissionMeta;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.PermissionException;
import com.github.jerrymice.permission.util.AnnotationUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author tumingjian
 * @date 2018/9/23
 * 说明:
 */
public class PermissionEngineProcessor {
    private PermissionEngine engine;
    private Permission[] permissions;
    private PermissionResult[] permissionResults;
    private MethodInvocation methodInvocation;
    private Parameter[] parameters;
    private Object[] arguments;
    private Class<?> returnType;
    private PermissionRejectProcessor rejectProcessor;
    private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();


    public PermissionEngineProcessor(MethodInvocation methodInvocation, PermissionEngine engine, PermissionRejectProcessor rejectProcessor) {
        this.methodInvocation = methodInvocation;
        this.engine = engine;
        this.permissions = AnnotationUtils.getPermissionAnnotation(this.methodInvocation.getMethod());
        this.permissionResults = AnnotationUtils.getPermissionResultAnnotation(this.methodInvocation.getMethod());
        this.parameters = methodInvocation.getMethod().getParameters();
        this.arguments = methodInvocation.getArguments();
        this.returnType = methodInvocation.getMethod().getReturnType();
        this.rejectProcessor = rejectProcessor;
    }

    /**
     * 判断权限是否足够
     *
     * @return 函数返回值
     * @throws Throwable 异常信息
     */
    protected boolean isPermission() throws Throwable {
        boolean isPermission = false;
        for (Permission per : permissions) {
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
        return isPermission;
    }

    protected Object[] methodParameterProcess() {
        Object[] result = this.arguments;
        for (int i = 0; i < parameters.length; i++) {
            PermissionMeta annotation = parameters[i].getAnnotation(PermissionMeta.class);
            if (annotation != null) {
                Object object = methodParameterProcess(engine, parameters[i], annotation, null);
                result[i] = object;
            }
        }
        return result;
    }

    /**
     * 处理单个注解参数
     *
     * @param engine     permission引擎
     * @param parameter  当前参数
     * @param annotation 当前参数上的PersissionMate注解
     * @param value      当前参数的原始值.
     * @return 返回当前参数处理后的新值
     */
    private Object methodParameterProcess(PermissionEngine engine, Parameter parameter, PermissionMeta annotation, Object value) {
        Object result = value;
        /**
         * 处理PermissionMeta默认值
         */
        if (result == null && !StringUtils.isEmpty(annotation.defaultValue())) {
            result = engine.eval(annotation.defaultValue());
        }
        /**
         * 定义JS变量
         */
        String varName = StringUtils.isEmpty(annotation.var()) ? parameter.getName() : annotation.var();
        engine.put(varName, result);
        /**
         * 处理PermissionMeta注解eval
         */
        if (!StringUtils.isEmpty(annotation.eval())) {
            result = engine.eval(annotation.eval());
        }
        /**
         * 将结果转换为方法参数的类型.
         */
        return converter(result,parameter.getType());
    }


    /**
     * @param beforeResult
     * @return
     */
    protected Object eachPermissionResult(Object beforeResult) {
        Object result = beforeResult;
        for (PermissionResult annotation : permissionResults) {
            result = permissionResultProcess(annotation, result);
        }

        return converter(result,beforeResult!=null?beforeResult.getClass():returnType);
    }

    private Object converter(Object result,Class<?> clazz) {
        if (result != null && !result.getClass().isAssignableFrom(clazz)) {
            return objectMapper.convertValue(result, clazz);
        } else {
            return result;
        }
    }

    private Object permissionResultProcess(PermissionResult annotation, Object body) {
        Object result = body;
        if (!annotation.ignoreNullValue() || body != null) {
            String value = annotation.eval();
            String returnVar = annotation.returnVar();
            String var = annotation.var();
            try {
                engine.put(var, body);
                if (!returnVar.equals(var)) {
                    engine.put(returnVar, "{}");
                }
            } catch (Exception e) {
                throw new PermissionException(e);
            }
            engine.eval(value);
            result = engine.eval(var);
        }
        return result;
    }

    public Object process() throws Throwable {
        Method method = methodInvocation.getMethod();
        if (permissions != null) {
            /**
             * 判断权限是否足够
             */
            boolean permission = this.isPermission();
            /**
             * 权限不足时直接返回权限处理结果.
             */
            if (!permission) {
                Object o = this.rejectProcessor.rejectProcess(engine);
                return o;
            }
        }
        /**
         * 目标方法参数处理
         */
        Object[] args = this.methodParameterProcess();
        /**
         *调用目标方法
         */
        Object proxy = methodInvocation.getThis();
        Object result = method.invoke(proxy, args);
        if (permissionResults != null) {
            result = eachPermissionResult(result);
        }
        return result;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public PermissionEngine getEngine() {
        return engine;
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }
}
