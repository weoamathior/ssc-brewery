package guru.sfg.brewery.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * Created by jt on 7/17/20.
 */
@Configuration
public class SecurityBeans {

    @Bean
    public GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository){
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder
                = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();

        configBuilder
                // magically set this value to 30 (from 60) to make things work
                // see https://www.udemy.com/course/spring-security-core-beginner-to-guru/learn/lecture/21369384#questions
                .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
                .setWindowSize(10)
                .setNumberOfScratchCodes(0);

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(configBuilder.build());
        googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;

    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher){
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

}