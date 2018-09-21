package com.github.jerrymice.permission.starter;

import com.github.jerrymice.permission.spring.PermissionEngineAspect;
import com.github.jerrymice.permission.config.PermissionConfig;
import com.github.jerrymice.permission.config.PermissionEngineGenerator;
import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.config.PermissionRejectProcessor;
import com.github.jerrymice.permission.factory.PermissionEngineFactory;
import com.github.jerrymice.permission.factory.support.WebPermissionEngineFactory;
import com.github.jerrymice.permission.spring.PermissionParamWebArgumentResolver;
import com.github.jerrymice.permission.spring.PermissionResponseBodyAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */

@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.jerrymice.permission.enabled", havingValue = "true")
public class PermissionEngineAutoConfiguration {
    @Autowired
    private JerryMicePermissionProperties permissionProperties;

    @Configuration
    @ConditionalOnMissingBean(PermissionEngineGenerator.class)
    public static class SpringPermissionEngineGenerator implements PermissionEngineGenerator {
        @Autowired
        @Lazy
        private HttpSession session;

        @Override
        public Object getKey(PermissionLoader permissionLoader) {
            return session;
        }
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    @ConditionalOnMissingBean(PermissionEngineFactory.class)
    public PermissionEngineFactory permissionFactory(
            PermissionLoader loader,
            PermissionEngineGenerator storeKeyGenerator,
            @Autowired(required = false) PermissionRejectProcessor processor) {
        WebPermissionEngineFactory webPermissionEngineFactory = new WebPermissionEngineFactory();
        webPermissionEngineFactory.setConfig(new PermissionConfig().setMixtureSearch(permissionProperties.isMixtureSearch()));
        webPermissionEngineFactory.setGenerator(storeKeyGenerator);
        webPermissionEngineFactory.setLoader(loader);
        if (processor != null) {
            webPermissionEngineFactory.setRejectProcessor(processor);
        }
        return webPermissionEngineFactory;
    }

    @Bean
    public PermissionResponseBodyAdvice permissionRequestResponseBodyAdvice(PermissionEngineFactory factory) {
        return new PermissionResponseBodyAdvice(factory);
    }

    @Bean
    @ConditionalOnMissingBean(PermissionEngineAspect.class)
    public PermissionEngineAspect permissionEngineAspect(PermissionEngineFactory factory) {
        return new PermissionEngineAspect(factory);
    }

    @Configuration
    public static class PermissionEngineWebMvcConfigurer implements WebMvcConfigurer {
        @Autowired
        private ApplicationContext context;
        @Autowired
        private PermissionEngineFactory factory;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new PermissionParamWebArgumentResolver(context, factory));
        }
    }

}
