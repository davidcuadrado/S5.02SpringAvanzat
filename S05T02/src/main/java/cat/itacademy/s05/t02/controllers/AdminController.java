package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.services.PetService;
import cat.itacademy.s05.t02.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "App", description = "the Application API")
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService gameService;
	@Autowired
	private PetService playerService;
	
	@Operation(summary = "Admin home page", description = "Home page for logged in admins.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleAdminWelcome() {
		return Mono.just(ResponseEntity.ok("Welcome back, you are now logged in!"));
	}

}
