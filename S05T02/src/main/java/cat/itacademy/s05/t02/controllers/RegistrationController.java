package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
public class RegistrationController {
	
	@Autowired
	private MyUserRepository myUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Operation(summary = "User registration", description = "Endpoint for user registration")
	@PostMapping("/register/user")
	public Mono<ResponseEntity<MyUser>> createUser(@RequestBody MyUser user) {
		return Mono.just(user)
			.map(userModify -> {
				userModify.setPassword(passwordEncoder.encode(userModify.getPassword()));
				return userModify;
			})
			.flatMap(myUserRepository::save)
			.map(savedUser -> ResponseEntity.ok(savedUser))
			.defaultIfEmpty(ResponseEntity.badRequest().build());
	}
}
