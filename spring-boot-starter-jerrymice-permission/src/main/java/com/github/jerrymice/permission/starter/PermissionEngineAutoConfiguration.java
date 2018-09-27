package com.github.jerrymice.permission.starter;


import com.github.jerrymice.permission.boot.PermissionEngineWebArgumentResolver;
import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import com.github.jerrymice.permission.factory.support.WebPermissionEngineFactory;
import com.github.jerrymice.permission.advisor.PermissionEngineAdvisor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;


import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author tumingjian
 * 说明:jerrymicePermission的spring boot 配置类
 */

@ConditionalOnWebApplication
public class PermissionEngineAutoConfiguration {
    @Autowired
    private JerryMicePermissionProperties permissionProperties;

    /**
     * 装载 RequestContextListener
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * 装载  PermissionEngineGenerator 在web环境下的配置类
     */
    @Configuration
    @ConditionalOnMissingBean(PermissionEngineGenerator.class)
    @Lazy
    public static class WebMvcPermissionEngineGenerator implements PermissionEngineGenerator {
        @Autowired
        private HttpSession session;

        @Override
        public Object getKey(PermissionService permissionLoader) {
            return session;
        }
    }

    /**
     * 装载  PermissionEngineFactory
     * @param permissionService   当前用户权限信息获取类
     * @param storeKeyGenerator  脚本引擎生成器
     * @param processor    自定义的权限不足时的拒绝信息处理者
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PermissionEngineFactory.class)
    public PermissionEngineFactory permissionFactory(
            PermissionService permissionService,
            PermissionEngineGenerator storeKeyGenerator,
            @Autowired(required = false) PermissionRejectProcessor processor) {
        WebPermissionEngineFactory webPermissionEngineFactory = new WebPermissionEngineFactory();
        PermissionConfig permissionConfig = new PermissionConfig()
                .setMixtureSearch(permissionProperties.isMixtureSearch())
                .setThreadLocalCache(permissionProperties.isThreadLocalCache());
        webPermissionEngineFactory.setConfig(permissionConfig);

        webPermissionEngineFactory.setGenerator(storeKeyGenerator);
        webPermissionEngineFactory.setPermissionService(permissionService);
        if (processor != null) {
            webPermissionEngineFactory.setRejectProcessor(processor);
        }
        return webPermissionEngineFactory;
    }

    /**
     * 装载 AOP织入者
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PermissionEngineAdvisor.class)
    public PermissionEngineAdvisor permissionEngineAspect() {
        return new PermissionEngineAdvisor();
    }

    /**
     * 装载 在controller的方法参数中可以获取当前PermissionEngine 的 WebArgumentResolver
     * @return
     */
    @Bean
    public PermissionEngineWebArgumentResolver permissionEngineWebArgumentResolver() {
        return new PermissionEngineWebArgumentResolver();
    }

    /**
     *把 PermissionEngineWebArgumentResolver 配置到spring mvc
     */
    @Configuration
    public class PermissionWebMvcConfigurer implements WebMvcConfigurer {
        @Autowired
        private PermissionEngineWebArgumentResolver resolver;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new ServletWebArgumentResolverAdapter(resolver));
        }
    }


}
