package com.github.jerrymice.permission.advisor;


import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author tumingjian
 * 说明:  切入点.该切入点只会对所有Controller(有@Controller或@RestController注解)类的(且有@RequestMapping注解)方法进行切入.
 */
public class PermissionEnginePointCut implements Pointcut {
    private ClassFilter classFilter=new ControllerClassFilter();
    private AnnotationMethodMatcher methodMatcher=new AnnotationMethodMatcher(RequestMapping.class);
    @Override
    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }
    public class ControllerClassFilter implements ClassFilter{
        @Override
        public boolean matches(Class<?> clazz) {
            return AnnotationUtils.findAnnotation(clazz, Controller.class)!=null || AnnotationUtils.findAnnotation(clazz, RestController.class)!=null;
        }
    }
}
