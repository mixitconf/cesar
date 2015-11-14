package org.mixit.cesar.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableCaching
public class CesarCacheConfig {

    public static final String CACHE_SECURITY = "security";
    public static final String CACHE_MEMBER = "member";
    public static final String CACHE_SESSION = "session";
    public static final String CACHE_ARTICLE = "article";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_SECURITY, CACHE_MEMBER, CACHE_SESSION, CACHE_ARTICLE);
    }


}
