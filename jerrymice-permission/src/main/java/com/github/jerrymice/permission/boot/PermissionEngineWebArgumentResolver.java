package com.github.jerrymice.permission.boot;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;


/**
 * @author tumingjian
 *         说明:spring web mvc PermissionEngine参数解析器.能在controller层的方法中自动注入有参数类型为PermissionEngine 的参数值
 */
public class PermissionEngineWebArgumentResolver implements WebArgumentResolver, ApplicationContextAware{
    private PermissionEngineFactory factory;

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        boolean assignableFrom = methodParameter.getParameterType().isAssignableFrom(PermissionEngine.class);
        if (assignableFrom) {
            PermissionEngine permissionEngine = factory.getPermissionEngine();
            return permissionEngine;
        }
        return UNRESOLVED;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PermissionEngineFactory bean = applicationContext.getBean(PermissionEngineFactory.class);
        this.factory=bean;
    }
}
