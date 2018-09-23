package com.github.jerrymice.permission.spring;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import java.io.Serializable;

/**
 * @author tumingjian
 * @date 2018/9/23
 * 说明:
 */
public class PermissionEngineAdvisor implements PointcutAdvisor, ApplicationContextAware, Ordered, Serializable {
    private PermissionEnginePointCut pointcut;
    private PermissionEngineAdvice advice;
    private int order;

    public PermissionEngineAdvisor() {
        this.pointcut = new PermissionEnginePointCut();
        this.advice = new PermissionEngineAdvice();
    }

    @Override
    public PermissionEnginePointCut getPointcut() {
        return pointcut;
    }

    @Override
    public PermissionEngineAdvice getAdvice() {
        return advice;
    }


    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean isPerInstance() {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.advice.context=applicationContext;
    }

    public void setPointcut(PermissionEnginePointCut pointcut) {
        this.pointcut = pointcut;
    }

    public void setAdvice(PermissionEngineAdvice advice) {
        this.advice = advice;
    }

    /**
     * advice
     */
    public class PermissionEngineAdvice implements MethodInterceptor {
        private PermissionEngineFactory factory;
        private ApplicationContext context;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (factory == null) {
                this.factory = context.getBean(PermissionEngineFactory.class);
            }
            PermissionEngine engine = factory.getPermissionEngine();
            PermissionEngineProcessor processor = new PermissionEngineProcessor(invocation, engine, factory.getRejectProcessor());
            Object result = processor.process();
            return result;
        }
    }

}
