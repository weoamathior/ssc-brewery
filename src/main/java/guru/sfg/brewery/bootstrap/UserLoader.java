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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("Running userloader");
        Authority createBeer = makeAuthority("beer.create");
        Authority updateBeer = makeAuthority("beer.update");
        Authority deleteBeer = makeAuthority("beer.delete");
        Authority readBeer = makeAuthority("beer.read");

        Authority createCustomer = makeAuthority("customer.create");
        Authority updateCustomer = makeAuthority("customer.update");
        Authority deleteCustomer = makeAuthority("customer.delete");
        Authority readCustomer = makeAuthority("customer.read");

        Authority createBrewery = makeAuthority("brewery.create");
        Authority updateBrewery = makeAuthority("brewery.update");
        Authority deleteBrewery = makeAuthority("brewery.delete");
        Authority readBrewery = makeAuthority("brewery.read");

        // beer order
        Authority createOrder = makeAuthority("order.create");
        Authority updateOrder = makeAuthority("order.update");
        Authority deleteOrder = makeAuthority("order.delete");
        Authority readOrder = makeAuthority("order.read");

        // customer beer order
        Authority createOrderCustomer = makeAuthority("customer.order.create");
        Authority updateOrderCustomer = makeAuthority("customer.order.update");
        Authority deleteOrderCustomer = makeAuthority("customer.order.delete");
        Authority readOrderCustomer = makeAuthority("customer.order.read");

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, deleteBeer, readBeer,
                createCustomer, updateCustomer, deleteCustomer, readCustomer,
                createBrewery, updateBrewery, deleteBrewery, readBrewery,
                createOrder, updateOrder, deleteOrder, readOrder
        )));

        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery,
                createOrderCustomer, updateOrderCustomer, deleteOrderCustomer, readOrderCustomer
        )));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        User adminUser = User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole).build();
        User userUser = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole).build();
        User scott = User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole).build();
        userRepository.save(adminUser);
        userRepository.save(userUser);
        userRepository.save(scott);

        log.info("how many? " + userRepository.count());
    }

    private Authority makeAuthority(String authKey) {
        return authorityRepository.save(Authority.builder().authority(authKey).build());
    }
}
