package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        log.info("Running userloader");
        Authority adminAuth = Authority.builder().authority("ROLE_ADMIN").build();
        Authority userAuth = Authority.builder().authority("ROLE_USER").build();
        Authority customerAuth = Authority.builder().authority("ROLE_CUSTOMER").build();
        authorityRepository.save(adminAuth);
        authorityRepository.save(userAuth);
        authorityRepository.save(customerAuth);

        User adminUser = User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .authority(adminAuth).build();
        User userUser= User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .authority(userAuth).build();
        User scott= User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .authority(customerAuth).build();
        userRepository.save(adminUser);
        userRepository.save(userUser);
        userRepository.save(scott);

        log.info("how many? " + userRepository.count());
//                .withUser("spring").password("{bcrypt}$2a$16$L8UgxnnceGLrMNkJpxYQLuY6UL9MYmER4N8JSuyDnNb9L7UyQGYu6").roles("ADMIN").and()
//                .withUser("user").password("{sha256}8a212c3cde9005dd9a52044794e16d7bc879334907489c42ab30a2b1b6cd56290d817859281810dd").roles("USER")
//                .and()
//                .withUser("scott").password("{bcrypt10}$2a$10$hjI0kXS/ZH.zPyII9CTNZ.ZfM2wDkSz.5Cht06MNhm97TavYxwkvy").roles("CUSTOMER");
    }
}
