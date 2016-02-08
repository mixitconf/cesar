package org.mixit.cesar.security.config;

import org.mixit.cesar.security.web.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class CesarSecurityConfig {
    @Bean
    public Filter securityFilter() {
        return new AuthenticationFilter()
                .addPathPatterns(
                        "/app/**/*",
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
