package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RegistrationController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Operation(summary = "User registration", description = "Endpoint for user registration")
	@PostMapping("/register/user")
	public MyUser createUser(@RequestBody MyUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
}
