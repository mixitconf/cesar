package org.mixit.cesar.site.config;

import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CesarHttpCacheConfig {

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry
                        .addResourceHandler("/**/*.html")
                        .addResourceLocations("classpath:/static/")
                        .setCacheControl(CacheControl.maxAge(10, TimeUnit.DAYS));
            }
        };
    }


    @Bean
    public Filter securityFilter() {
        return new ShallowEtagHeaderFilter();
    }

}