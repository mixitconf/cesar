package org.mixit.cesar.security.config;

import javax.servlet.Filter;

import org.mixit.cesar.security.service.authentification.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CesarSecurityConfig {

    @Bean
    public Filter securityFilter() {
        return new AuthenticationFilter()
                .addPathPatterns(
                        "/app/**/*",
                        "/cfp/**/*",
                        "/monitoring/**/*"
                )
                .excludePathPatterns(
                        "/app/login",
                        "/app/login-with/*",
                        "/app/oauth/*",
                        "/app/account/cesar",
                        "/app/account/cesar/*",
                        "/app/account/valid",
                        "/app/account/password");
    }
}
