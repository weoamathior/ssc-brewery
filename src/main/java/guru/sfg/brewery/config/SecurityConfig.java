package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        // using the custom factory
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * The intent: No authentication on the home page or login page.  No authentication on static resources
         */
        http

                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/brewery/breweries/**","/brewery/api/v1/breweries")
                                .hasAnyRole("CUSTOMER", "ADMIN")
                            .mvcMatchers("/beers/find", "/beers/{beerId}")
                                .hasAnyRole("CUSTOMER","ADMIN","USER");

                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic().and()
                .csrf().disable();

        // h2-console config (spring security blocks frames)
        http.headers().frameOptions().sameOrigin();

    }

}
