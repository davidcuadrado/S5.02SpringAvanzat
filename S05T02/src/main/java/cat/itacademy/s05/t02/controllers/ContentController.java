package cat.itacademy.s05.t02.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ContentController {
	
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
		return "You are now logged in, welcome!";
	}
	

}
