package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import cat.itacademy.s05.t02.token.LoginForm;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ContentController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MyUserDetailService myUserDetailService;

	@Operation(summary = "Home page", description = "Home page for general users. ")
	@GetMapping("/home")
	public String handleWelcome() {
		return "You are in the home site";
	}

	@Operation(summary = "User home page", description = "Home page for logged in users. ")
	@GetMapping("/user/home")
	public String handleUserWelcome() {
		return "You are now logged in, welcome!";
	}

	@Operation(summary = "Admin home page", description = "Home page for logged in admins. ")
	@GetMapping("/admin/home")
	public String handleAdminWelcome() {
		return "You are now logged in as admin, welcome!";
	}

	@Operation(summary = "Login page", description = "Login page for all users ")
	@GetMapping("/login")
	public String handleLogin() {
		return "custom_login";
	}

	@Operation(summary = " ", description = " ")
	@PostMapping("/authenticate")
	public String authenticateAndGetToekn(@RequestBody LoginForm loginForm) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
		if ( authentication.isAuthenticated()) {
			return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
		} else {
			throw new UsernameNotFoundException("Invalid credentials");
		}
	}

}
