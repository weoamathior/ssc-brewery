package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        log.info("Running userloader");
        Authority createBeer = authorityRepository.save(Authority.builder().authority("beer.create").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().authority("beer.update").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().authority("beer.delete").build());
        Authority readBeer = authorityRepository.save(Authority.builder().authority("beer.read").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(Set.of(createBeer,updateBeer,deleteBeer,readBeer));
        customerRole.setAuthorities(Set.of(readBeer));
        userRole.setAuthorities(Set.of(readBeer));

        roleRepository.saveAll(Arrays.asList(adminRole,customerRole, userRole));

        User adminUser = User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole).build();
        User userUser= User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole).build();
        User scott= User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole).build();
        userRepository.save(adminUser);
        userRepository.save(userUser);
        userRepository.save(scott);

        log.info("how many? " + userRepository.count());
    }
}
