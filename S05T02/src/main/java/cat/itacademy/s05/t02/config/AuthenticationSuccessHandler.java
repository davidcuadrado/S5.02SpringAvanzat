package cat.itacademy.s05.t02.config;

import java.net.URI;

import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/*
@Component
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
	    ServerWebExchange exchange = webFilterExchange.getExchange();

	    // Exclude /home/login from redirection
	    if (exchange.getRequest().getPath().toString().equals("/home/login")) {
	        return exchange.getResponse().setComplete();
	    }

	    return Mono.defer(() -> {
	        boolean isAdmin = authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

	        String redirectUrl = isAdmin ? "/admin/home" : "/user/home";
	        setRedirectResponse(exchange, redirectUrl);

	        return exchange.getResponse().setComplete();
	    });
	}

}
*/