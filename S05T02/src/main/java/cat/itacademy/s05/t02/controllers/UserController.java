package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.repositories.MyUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "User", description = "the User API")
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private MyUserRepository userRepository;
	
	

	@Operation(summary = "User home page", description = "Home page for logged in users.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleUserWelcome() {
		return Mono.just(ResponseEntity.ok("You are now logged in, welcome!"));
	}
	
	@Operation(summary = "Create a new pet", description = "Create a new user's pet ")
	@PostMapping("/create")
	public Mono<ResponseEntity<Pet>> createNewPet(@RequestBody String newPet) {
		return petService.createNewPet(Mono.just(newPet))
				.flatMap(player -> gameService.createNewGame(Mono.just(player))
						.map(newGame -> ResponseEntity.status(HttpStatus.CREATED).body(newGame))
						.onErrorMap(e -> new IllegalArgumentException("Error saving new game. ")));
	}

}
