package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
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
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                                .hasAnyRole("CUSTOMER","ADMIN","USER")

                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .mvcMatchers(HttpMethod.GET, "/brewery/breweries/**","/brewery/api/v1/breweries")
                                .hasAnyRole("CUSTOMER", "ADMIN")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                                .hasAnyRole("CUSTOMER","ADMIN","USER")
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


//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder().username("spring").password("guru").roles("ADMIN").build();
//        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
//
//        return new InMemoryUserDetailsManager(admin,user);
//
//    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder().username("spring").password("guru").roles("ADMIN").build();
//        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
//        return new
//    }


    /**
     * This is an alternative "fluent" configuration to create the UserDetailsService
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("spring").password("{bcrypt}$2a$16$L8UgxnnceGLrMNkJpxYQLuY6UL9MYmER4N8JSuyDnNb9L7UyQGYu6").roles("ADMIN").and()
//                .withUser("user").password("{sha256}8a212c3cde9005dd9a52044794e16d7bc879334907489c42ab30a2b1b6cd56290d817859281810dd").roles("USER")
//                .and()
//                .withUser("scott").password("{bcrypt10}$2a$10$hjI0kXS/ZH.zPyII9CTNZ.ZfM2wDkSz.5Cht06MNhm97TavYxwkvy").roles("CUSTOMER");
//
//    }
}
