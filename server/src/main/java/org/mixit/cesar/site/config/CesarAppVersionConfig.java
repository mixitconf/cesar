package org.mixit.cesar.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value= "classpath:version.yml")
@Profile("cloud")
public class CesarAppVersionConfig {

    /**
     * @see org.mixit.cesar.CesarInitializer
     */
    @Bean
    public String cesarInitializer(){
        return "not use in cloud profile";
    }
}
