package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import cat.itacademy.s05.t02.token.LoginForm;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
public class ContentController {

	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MyUserDetailService myUserDetailService;

	@Operation(summary = "Home page", description = "Home page for general users.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleWelcome() {
		return Mono.just(ResponseEntity.ok("You are in the home site"));
	}

	@Operation(summary = "Login page", description = "Login page for all users")
	@GetMapping("/login")
	public Mono<ResponseEntity<String>> handleLogin() {
		return Mono.just(ResponseEntity.ok("custom_login"));
	}

	@Operation(summary = "Verify authentication", description = "Endpoint for authenticate validation and token register.")
	@PostMapping("/authenticate")
	public Mono<ResponseEntity<String>> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
	    return authenticationManager
	            .authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()))
	            .flatMap(authentication -> {
	                if (authentication.isAuthenticated()) {
	                    return myUserDetailService.findByUsername(loginForm.username())
	                            .flatMap(userDetails -> jwtService.generateToken(Mono.just(userDetails)))
	                            .map(ResponseEntity::ok);
	                } else {
	                    return Mono.error(new UsernameNotFoundException("Invalid credentials"));
	                }
	            })
	            .onErrorResume(e -> Mono.just(ResponseEntity.status(401).body("Authentication failed")));
	}


}
