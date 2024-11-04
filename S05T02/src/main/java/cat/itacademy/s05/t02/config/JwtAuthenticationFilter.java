package cat.itacademy.s05.t02.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import reactor.core.publisher.Mono;

@Configuration
public class JwtAuthenticationFilter implements WebFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private MyUserDetailService myUserDetailService;

	
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
	    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return chain.filter(exchange);
	    }

	    String jwt = authHeader.substring(7);

	    return jwtService.extractUsername(jwt)
	        .flatMap(username -> {
	            if (username == null) {
	                return chain.filter(exchange);
	            }
	            return myUserDetailService.findByUsername(username)
	                .flatMap(userDetails -> {
	                    UsernamePasswordAuthenticationToken authentication = 
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	                    SecurityContext context = new SecurityContextImpl(authentication);
	                    return ((Mono<Void>) ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)))
	                        .then(chain.filter(exchange));
	                });
	        })
	        .onErrorResume(e -> {
	            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	            return exchange.getResponse().setComplete();
	        });
	}


}
