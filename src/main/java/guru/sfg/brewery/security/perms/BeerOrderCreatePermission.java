package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("hasAuthority('order.create') OR " +
        "hasAuthority('customer.order.create') AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
@Retention(RetentionPolicy.RUNTIME)
public @interface BeerOrderCreatePermission {
}
