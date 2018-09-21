package com.github.jerrymice.permission.spring;

import com.github.jerrymice.permission.annotation.PermissionMeta;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;


/**
 * @author tumingjian
 * @date 2018/9/18
 * 说明:
 */

public class PermissionParamWebArgumentResolver implements HandlerMethodArgumentResolver {

    private PermissionEngineFactory factory;
    private ApplicationContext context;

    public PermissionParamWebArgumentResolver(ApplicationContext context, PermissionEngineFactory factory) {
        this.factory = factory;
        this.context = context;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        PermissionMeta annotation = parameter.getParameterAnnotation(PermissionMeta.class);
        return annotation!=null || PermissionEngine.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        PermissionEngine engine=factory.getPermissionEngine();
        if(PermissionEngine.class.isAssignableFrom(parameter.getParameterType())){
            return engine;
        }
        PermissionMeta annotation = parameter.getParameterAnnotation(PermissionMeta.class);
        Object result = null;
        RequestMappingHandlerAdapter bean = context.getBean(RequestMappingHandlerAdapter.class);
        List<HandlerMethodArgumentResolver> argumentResolvers = bean.getArgumentResolvers();
        /**
         * 查找合适的处理器,先处理spring mvc的注解和参数转换
         */
        for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
            if (resolver instanceof PermissionParamWebArgumentResolver) {
                continue;
            }
            if (resolver.supportsParameter(parameter)) {
                result = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
            }
        }
        /**
         * 处理PermissionMeta默认值
         */
        if (result == null && !StringUtils.isEmpty(annotation.defaultValue())) {
            result = engine.eval(annotation.defaultValue());
        }
        /**
         * 定义JS变量
         */
        String varName = StringUtils.isEmpty(annotation.var()) ? parameter.getParameterName() : annotation.var();
        engine.put(varName, result);
        System.out.println("第一次:"+engine);
        /**
         * 处理PermissionMeta注解eval
         */
        if (!StringUtils.isEmpty(annotation.eval())) {
            result = engine.eval(annotation.eval());
        }
        /**
         * 将结果转换为方法参数的类型.
         */
        if (result != null && !result.getClass().isAssignableFrom(parameter.getParameterType())) {
            result = Jackson2ObjectMapperBuilder.json().build().convertValue(result, parameter.getParameterType());
        }
        return result;
    }
}
