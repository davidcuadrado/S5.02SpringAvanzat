package cat.itacademy.s05.t02.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
	private MyUserDetailService userDetailService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String path = exchange.getRequest().getPath().value();
		if (path.startsWith("/register") || path.startsWith("/authenticate") || path.startsWith("/home/login")
				|| path.startsWith("/swagger")) {
			return chain.filter(exchange);
		}

		String token = extractToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

		return Mono.justOrEmpty(token).flatMap(jwtService::validateAndExtractUsername)
				.flatMap(userDetailService::findByUsername).map(userDetails -> {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					return new SecurityContextImpl(auth);
				})
				.flatMap(securityContext -> chain.filter(exchange)
						.contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))))

				.onErrorResume(e -> {
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				});

	}

	public String extractToken(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return null;
		}
		return header.substring(7);
	}
}
