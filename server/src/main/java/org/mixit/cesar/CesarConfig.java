package org.mixit.cesar;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class CesarConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Docket cesarApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("full-petstore-api")
                .apiInfo(apiInfo())
                .select()
                .paths(or(
                        regex("/api/member.*"),
                        regex("/api/session.*")
                ))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Cesar API")
                .description("You can use the cesar API to read informations about Mix-IT conference")
                .contact("contact@mix-it.fr")
                .license("MIT licence")
                .licenseUrl("http://opensource.org/licenses/MIT")
                .version("2.0")
                .build();
    }
}
