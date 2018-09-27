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
 * 说明:负责处理所有权限相关的注解方法与参数
 */
public class PermissionEngineProcessor {
    /**
     * 处理当前方法权限的Engine
     */
    private PermissionEngine engine;
    /**
     * 当前方法的@Permission数组
     */
    private Permission[] permissions;
    /**
     * 当前方法的@PermissionResult数组
     */
    private PermissionResult[] permissionResults;
    /**
     * 当前方法的AOP拦截对象
     */
    private MethodInvocation methodInvocation;
    /**
     * 当前方法参数
     */
    private Parameter[] parameters;
    /**
     * 当前方法参数的值对象
     */
    private Object[] arguments;
    /**
     * 当前方法的返回值类型
     */
    private Class<?> returnType;
    /**
     * 权限不足时的后续结果处理拦截对象
     */
    private PermissionRejectProcessor rejectProcessor;
    /**
     * json转换.用于将一个结果对象转换为方法的返回值对象
     */
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

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public PermissionEngine getEngine() {
        return engine;
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    /**
     * 循环处理该方法上所有有@PermissionMeta的参数
     * @return  返回处理后该方法参数的数组
     */
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
     * 处理单个PermissionMeta注解参数
     *
     * @param engine     permission引擎
     * @param parameter  当前参数
     * @param annotation 当前参数上的PermissionMate注解
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
     * 循环处理该方法上有一个或多个@PermissionResult的返回值
     * @param beforeResult  处理前方法的返回值.
     * @return  返回处理后该方法的返回值
     */
    protected Object eachPermissionResult(Object beforeResult) {
        Object result = beforeResult;
        for (PermissionResult annotation : permissionResults) {
            result = permissionResultProcess(annotation, result);
        }

        return converter(result,beforeResult!=null?beforeResult.getClass():returnType);
    }

    /**
     * 对象转换
     * @param result  要转换的对象
     * @param clazz  要转换的类
     * @return 转换后的值
     */
    private Object converter(Object result,Class<?> clazz) {
        if (result != null && !result.getClass().isAssignableFrom(clazz)) {
            return objectMapper.convertValue(result, clazz);
        } else {
            return result;
        }
    }

    /**
     * 处理单个 PermissionResult 注解
     * @param annotation   当前要处理的PermissionResult注解
     * @param beforeResult 处理前方法的返回值
     * @return 返回处理后该方法的返回值
     */
    private Object permissionResultProcess(PermissionResult annotation, Object beforeResult) {
        Object result = beforeResult;
        if (!annotation.ignoreNullValue() || beforeResult != null) {
            String value = annotation.eval();
            String returnVar = annotation.returnVar();
            String var = annotation.var();
            try {
                engine.put(var, beforeResult);
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

    /**
     * 该类的入口方法.负责调用其他方法,来完成对权限,方法参数,和方法结果的检查和控制
     * @return  返回该方法执行后的结果
     * @throws Throwable   异常信息
     */
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


}
