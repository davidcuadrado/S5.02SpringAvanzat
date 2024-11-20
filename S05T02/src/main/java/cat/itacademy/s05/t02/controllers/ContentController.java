	package cat.itacademy.s05.t02.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import cat.itacademy.s05.t02.token.LoginForm;
import cat.itacademy.s05.t02.token.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/home")
public class ContentController {

	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MyUserDetailService myUserDetailService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(ContentController.class);

	@Operation(summary = "Home page", description = "Home page for general users.")
	@GetMapping("")
	public Mono<ResponseEntity<String>> handleWelcome() {
		return Mono.just(ResponseEntity.ok("You are in the home site"));
	}

	@Operation(summary = "Login page", description = "Login page for all users")
	@PostMapping("/login")
	public Mono<ResponseEntity<Map<String, String>>> handleLogin(@Valid @RequestBody LoginForm loginForm) {
	    log.debug("Received login request for username: {}", loginForm.username());
	    
	    return myUserDetailService.findByUsernameMono(Mono.just(loginForm.username()))
	        .flatMap(user -> {
	            log.debug("User found: {}", user.getUsername());
	            if (passwordEncoder.matches(loginForm.password(), user.getPassword())) {
	                log.debug("Password matches for user: {}", user.getUsername());
	                return jwtService.generateToken(Mono.just(user))
	                    .map(token -> {
	                        String role = user.getAuthorities().stream()
	                            .map(GrantedAuthority::getAuthority)
	                            .findFirst()
	                            .orElse("USER");
	                        log.debug("Generated token for user: {}, role: {}", user.getUsername(), role);
	                        
	                        Map<String, String> responseBody = new HashMap<>();
	                        responseBody.put("token", token);
	                        responseBody.put("role", role);
	                        return ResponseEntity.ok(responseBody);
	                    });
	            } else {
	                log.warn("Invalid password for user: {}", user.getUsername());
	                return Mono.just(ResponseEntity
	                    .status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Invalid credentials")));
	            }
	        })
	        .switchIfEmpty(Mono.just(
	            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "User not found"))
	        ));
	}


	@Operation(summary = "Verify authentication", description = "Endpoint for authentication validation and token register.")
	@PostMapping("/authenticate")
	public Mono<ResponseEntity<LoginResponse>> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
		log.debug("Received authentication request for username: {}", loginForm.username());

		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()))
				.cast(UserDetails.class)
				.flatMap(userDetails -> jwtService.generateToken(Mono.just(userDetails)).map(token -> {
					String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
							.orElse("USER");
					log.debug("Generated token for user: {}, role: {}", userDetails.getUsername(), role);
					return ResponseEntity.ok(new LoginResponse(token, role));
				})).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new LoginResponse(null, "Authentication process error."))))
				.onErrorResume(e -> {
					log.error("Authentication failed: {}", e.getMessage(), e);
					return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(new LoginResponse(null, "Authentication failed.")));
				});
	}

}
