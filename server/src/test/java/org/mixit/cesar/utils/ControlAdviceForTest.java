package org.mixit.cesar.utils;

import org.mixit.cesar.web.WebControllerAdvice;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author GET <guillaume@dev-mind.fr>
 */
public class ControlAdviceForTest {

    /**
     * Create a context with the controller advice which contains errors resolvers
     */
    public static WebMvcConfigurationSupport createContextForTest() {
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerSingleton("webControllerAdvice", WebControllerAdvice.class);

        final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
        webMvcConfigurationSupport.setApplicationContext(applicationContext);

        return webMvcConfigurationSupport;
    }
}
