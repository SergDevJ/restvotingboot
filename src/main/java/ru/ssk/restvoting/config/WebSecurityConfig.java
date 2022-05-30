package ru.ssk.restvoting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import ru.ssk.restvoting.service.UserService;

//https://stackoverflow.com/questions/33603156/spring-security-multiple-http-config-not-working
//https://docs.spring.io/spring-security/site/docs/4.2.x/reference/htmlsingle/#multiple-httpsecurity

@Configuration
@EnableWebSecurity(debug=true)
public class WebSecurityConfig {
    @Autowired
    private UserService userService;

    public static class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    public void configUsers(AuthenticationManagerBuilder auth,
                            PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    //REST configuration
    @Configuration
    @EnableWebSecurity(debug=true)
    @Order(1)
    public static class RestConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/rest/**").
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                    httpBasic().and().
                    csrf().disable().
                    authorizeRequests().
                    antMatchers("/rest/admin/**").hasRole("ADMIN").
                    antMatchers("/rest/profile/register").anonymous().
                    antMatchers("/rest/**").authenticated();
        }
    }

    //Main configuration
    @Configuration
    @Order(2)
    public static class MainConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/resources/**", "/webjars/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().
                    antMatchers("/login",
                            "/profile/register",
                            "/swagger-ui/**",
                            "/v2/api-docs/**"
                            ).permitAll().and().
                    authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").and().
                    authorizeRequests().anyRequest().authenticated().and().
                    formLogin().loginPage("/login").permitAll().
                    defaultSuccessUrl("/").
                    failureUrl("/login?error=true").
                    loginProcessingUrl("/spring_security_check");
        }
    }
}
