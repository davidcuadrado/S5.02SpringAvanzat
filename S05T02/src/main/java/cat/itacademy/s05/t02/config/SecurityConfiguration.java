package cat.itacademy.s05.t02.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import cat.itacademy.s05.t02.services.MyUserDetailService;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Autowired
	private MyUserDetailService userDetailService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	/*
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeExchange(auth -> auth
						.matchers(ServerWebExchangeMatchers.pathMatchers("/home", "/register/**", "/authenticate/**",
								"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"))
						.permitAll().pathMatchers("/user/**", "/pet/**").hasRole("USER")
						.pathMatchers("/admin/**", "/user/**", "/pet/**").hasRole("ADMIN").anyExchange()
						.authenticated())
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.formLogin(
						formLoginSpec -> formLoginSpec.loginPage("/login").authenticationManager(entry -> Mono.empty()))
				.build();
	}
	*/

	 @Bean
	    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	        return http
	            .csrf(csrf -> csrf.disable())
	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .authorizeExchange(exchanges -> exchanges
	                .pathMatchers("/home/**", "/register/**", "/authenticate/**", "home/login", "home/authenticate").permitAll()
	                .pathMatchers("/user/**", "/pet/**").hasRole("USER")
	                .pathMatchers("/admin/**", "/user/**", "/pet/**").hasRole("ADMIN")
	                .anyExchange().authenticated()
	            )
	            .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
	                .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> {
	                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	                }))
	                .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> {
	                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
	                }))
	            )
	            .build();
	    }

	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.addAllowedOrigin("http://localhost:3000"); // Permite solicitudes desde el frontend
	        config.addAllowedMethod("*");                     // Permite todos los métodos HTTP
	        config.addAllowedHeader("*");                     // Permite todas las cabeceras
	        config.setAllowCredentials(true);                 // Permite cookies y autenticación

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	    }

	
	/*
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("http://localhost:3000");
		config.setAllowCredentials(true);
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		config.addAllowedHeader("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	}
	*/

	@Bean
	public ReactiveUserDetailsService userDetailsService() {
		return userDetailService;
	}

	@Bean
	public ReactiveAuthenticationManager authenticationManager() {
		return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
