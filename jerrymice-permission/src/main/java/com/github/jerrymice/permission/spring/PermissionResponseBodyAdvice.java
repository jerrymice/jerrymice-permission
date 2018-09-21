package com.github.jerrymice.permission.spring;

import com.github.jerrymice.permission.annotation.PermissionMeta;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.PermissionException;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author tumingjian
 * @date 2018/9/20
 * 说明:
 */
@ControllerAdvice()
public class PermissionResponseBodyAdvice<T> implements ResponseBodyAdvice<T>{
    private PermissionEngineFactory factory;

    public PermissionResponseBodyAdvice(PermissionEngineFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        PermissionResult annotation = returnType.getMethod().getAnnotation(PermissionResult.class);
        return annotation != null && !StringUtils.isEmpty(annotation.eval());
    }

    @Override
    public T beforeBodyWrite(@Nullable T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        PermissionResult annotation = returnType.getMethod().getAnnotation(PermissionResult.class);
        if (!annotation.ignoreNullValue() || body != null) {
            PermissionEngine engine = factory.getPermissionEngine();
            String value = annotation.eval();
            String returnVar = annotation.returnVar();
            String var = annotation.var();
            try {
                engine.put(var, body);
                if(!returnVar.equals(var)){
                    engine.put(returnVar,"{}");
                }
            } catch (Exception e) {
                throw new PermissionException(e);
            }
            engine.eval(value);
            Object result = engine.eval(var);
            if (result != null && !result.getClass().isAssignableFrom(returnType.getParameterType())) {
                return (T) Jackson2ObjectMapperBuilder.json().build().convertValue(result, returnType.getParameterType());
            }else{
                return null;
            }
        }
        return body;
    }

}
