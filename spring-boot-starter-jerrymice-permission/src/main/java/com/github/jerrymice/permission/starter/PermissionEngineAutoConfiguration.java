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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
 * @date 2018/9/14
 * 说明:
 */

@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.jerrymice.permission.enabled", havingValue = "true")
public class PermissionEngineAutoConfiguration {
    @Autowired
    private JerryMicePermissionProperties permissionProperties;
    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

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
    @Bean
    @ConditionalOnMissingBean(PermissionEngineFactory.class)
    public PermissionEngineFactory permissionFactory(
            PermissionService loader,
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
    @ConditionalOnMissingBean(PermissionEngineAdvisor.class)
    public PermissionEngineAdvisor permissionEngineAspect() {
        return new PermissionEngineAdvisor();
    }

    @Bean
    public PermissionEngineWebArgumentResolver permissionEngineWebArgumentResolver(){
        return new PermissionEngineWebArgumentResolver();
    }

    @Configuration
    @ConditionalOnBean(PermissionEngineWebArgumentResolver.class)
    public class PermissionWebMvcConfigurer implements WebMvcConfigurer{
        @Autowired
        private PermissionEngineWebArgumentResolver resolver;
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new ServletWebArgumentResolverAdapter(resolver));
        }
    }




}
