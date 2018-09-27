package com.github.jerrymice.permission.advisor;

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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tumingjian
 *         说明:Spring AOP织入者.负责在指定的切入点上织入通知类
 */
public class PermissionEngineAdvisor implements PointcutAdvisor, ApplicationContextAware, Ordered, Serializable {
    private final Map<Method, MethodInvocationInfo> cacheMethodInfo = new HashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    /**
     * 切入点
     */
    private PermissionEnginePointCut pointcut;
    /**
     * 通知类
     */
    private PermissionEngineAdvice advice;
    /**
     * 通知类的要织入的序号
     */
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
        this.advice.context = applicationContext;
    }

    public void setPointcut(PermissionEnginePointCut pointcut) {
        this.pointcut = pointcut;
    }

    public void setAdvice(PermissionEngineAdvice advice) {
        this.advice = advice;
    }

    /**
     * Advice通知类的实现类,负责把需要要切入的方法和方法参数交由PermissionEngineProcessor处理
     */
    public class PermissionEngineAdvice implements MethodInterceptor {
        private PermissionEngineFactory factory;
        private ApplicationContext context;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (factory == null) {
                this.factory = context.getBean(PermissionEngineFactory.class);
            }
            Method method = invocation.getMethod();
            MethodInvocationInfo methodInvocationInfo = null;
            readLock.lock();
            try {
                methodInvocationInfo = cacheMethodInfo.get(invocation.getMethod());
            } finally {
                readLock.unlock();
            }
            if (methodInvocationInfo != null) {
                return process(methodInvocationInfo, invocation);
            } else {
                methodInvocationInfo = new MethodInvocationInfo(invocation);
                if (methodInvocationInfo.getPermissionResults() != null || methodInvocationInfo.getPermissions() != null || methodInvocationInfo.getPermissionMetas() != null) {
                    writeLock.lock();
                    try{
                        cacheMethodInfo.put(method, methodInvocationInfo);
                    }finally {
                        writeLock.unlock();
                    }

                    return process(methodInvocationInfo, invocation);
                }
            }

            return invocation.proceed();
        }

        private Object process(MethodInvocationInfo methodInvocationInfo, MethodInvocation invocation) throws Throwable {
            PermissionEngine engine = factory.getPermissionEngine();
            Object result = null;
            try {
                PermissionEngineProcessor processor = new PermissionEngineProcessor(methodInvocationInfo, invocation, engine, factory.getRejectProcessor());
                result = processor.process();
            } finally {
                engine.release();
                factory.removePermissionEngine(false);
            }
            return result;
        }
    }

}
