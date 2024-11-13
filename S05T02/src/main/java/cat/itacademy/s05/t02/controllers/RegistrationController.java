package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "Register", description = "the Registration API")
@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private MyUserRepository myUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Operation(summary = "User registration", description = "Endpoint for user registration")
	@PostMapping("/new")
	public Mono<ResponseEntity<MyUser>> createUser(@RequestBody MyUser user) {
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    
	    // Log before saving
	    System.out.println("Attempting to save user: " + user);
	    
	    return myUserRepository.save(user)
	            .map(savedUser -> {
	                // Log after saving
	                System.out.println("User saved successfully: " + savedUser);
	                return ResponseEntity.ok(savedUser);
	            })
	            .defaultIfEmpty(ResponseEntity.badRequest().build());
	}
}
