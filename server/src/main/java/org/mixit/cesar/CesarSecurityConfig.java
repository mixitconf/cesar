package org.mixit.cesar;

import org.mixit.cesar.security.authentification.AuthenticationInterceptor;
import org.mixit.cesar.web.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class CesarSecurityConfig {
    //extends WebSecurityConfigurerAdapter {

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;




//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new StandardPasswordEncoder();
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        //auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
//
//        auth.inMemoryAuthentication().withUser("user").password("password").roles("ADMIN");
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/app/**").authenticated()
//                .antMatchers(HttpMethod.OPTIONS, "/app/**").permitAll()
//                .and()
//                .formLogin()
//                .loginPage(absoluteUrlFactory.getBaseUrl() + "authent")
//                    //.loginProcessingUrl("/app/login")
//                    .permitAll()
//                .and()
//                .logout()
//                    .logoutUrl("/app/logout")
//                    .logoutSuccessUrl("/")
//                    .permitAll()
//                .and()
//                    .httpBasic()
//
//
//        ;
//    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService()  {
//        return new CesarUserDetailsService();
//    }
//
//    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
//    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
//
//    }
}
