package cat.itacademy.s05.t02.config;

import java.net.URI;

import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();

        return Mono.defer(() -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            String redirectUrl = isAdmin ? "/admin/home" : "/user/home";
            setRedirectResponse(exchange, redirectUrl); // Extracted method

            return exchange.getResponse().setComplete();
        });
    }

    protected void setRedirectResponse(ServerWebExchange exchange, String redirectUrl) {
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
    }
}

