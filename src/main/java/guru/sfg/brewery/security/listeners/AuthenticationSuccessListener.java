package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginSuccess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener {

    private final LoginSuccessRepository loginSuccessRepository;

    @EventListener
    public void listen(AuthenticationSuccessEvent event) {
        log.debug("User logged in okay");
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            LoginSuccess.LoginSuccessBuilder builder = LoginSuccess.builder();

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            if (token.getPrincipal() instanceof User) {
                User user = (User) token.getPrincipal();
                log.debug("user is " + user.getUsername());
                builder.user(user);
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                log.debug("remote addr: " + details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());

            }
            LoginSuccess success = loginSuccessRepository.save(builder.build());
            log.debug("saved success " + success.getId());
        }
    }
}
