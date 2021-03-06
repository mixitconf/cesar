package org.mixit.cesar.site.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CesarCacheConfig {

    public static final String CACHE_SECURITY = "security";
    public static final String CACHE_MEMBER = "member";
    public static final String CACHE_SPONSOR = "sponsor";
    public static final String CACHE_SESSION = "session";
    public static final String CACHE_RANKING = "ranking";
    public static final String CACHE_SPEAKER_LT = "speakerlt";
    public static final String CACHE_LIGHTNINGTALK = "lightningtalk";
    public static final String CACHE_ARTICLE = "article";
    public static final String CACHE_ARTICLE_DETAIL = "article_detail";
    public static final String CACHE_PLANNING = "planning";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                CACHE_SECURITY,
                CACHE_MEMBER,
                CACHE_SPEAKER_LT,
                CACHE_SESSION,
                CACHE_RANKING,
                CACHE_LIGHTNINGTALK,
                CACHE_SPONSOR,
                CACHE_ARTICLE,
                CACHE_ARTICLE_DETAIL,
                CACHE_PLANNING);
    }


}
