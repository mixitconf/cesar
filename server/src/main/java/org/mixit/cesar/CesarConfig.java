package org.mixit.cesar;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.text.SimpleDateFormat;

import org.mixit.cesar.security.authentification.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class CesarConfig  {
                   
                   
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

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return b;
    }

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
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
                        .excludePathPatterns("/app/login", "/app/login-with/*", "/app/logout", "/app/oauth/*", "/app/account/check/*", "/app/account/create", "/app/account/create-finalize");
            }

        };
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
