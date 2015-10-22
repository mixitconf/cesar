package org.mixit.cesar.config;

import org.mixit.cesar.service.authentification.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CesarSecurityConfig {

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public WebMvcConfigurer securityConfigurer() {
        return new WebMvcConfigurerAdapter() {

            /**
             * Adds an interceptor to handle authentication. All the HTTP requests must have a header Custom-Authentication with their
             * login as value to be able to access the resource. Otherwise, a 401 response is sent back. The only URLs that are
             * not intercepted are /users (used to register) and /authentication (used to authenticate)
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(authenticationInterceptor())
                        .addPathPatterns("/app/**/*")
                        .excludePathPatterns(
                                "/app/login",
                                "/app/login-with/*",
                                "/app/logout",
                                "/app/oauth/*",
                                "/app/account/check/*",
                                "/app/account/create",
                                "/app/account/valid");
            }

        };
    }
}
