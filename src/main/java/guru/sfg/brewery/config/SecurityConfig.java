package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.google.Google2faFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final Google2faFilter google2faFilter;

    // needed for use with Spring Data JPA Spel
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(google2faFilter, SessionManagementFilter.class);

        /**
         * The intent: No authentication on the home page or login page.  No authentication on static resources
         */
        http

                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();

                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin(loginConfigurer -> {
                    loginConfigurer.loginProcessingUrl("/login")
                            .loginPage("/").permitAll()
                            .successForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureUrl("/?error");
                })
                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/?logout").permitAll();
                })
                .httpBasic()
                .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                .and()
                .rememberMe()
                .tokenRepository(this.persistentTokenRepository)
                .userDetailsService(this.userDetailsService);

        // hash-based remember me
        //.rememberMe().key("sfg-key").userDetailsService(this.userDetailsService);

        // h2-console config (spring security blocks frames)
        http.headers().frameOptions().sameOrigin();

    }

}
