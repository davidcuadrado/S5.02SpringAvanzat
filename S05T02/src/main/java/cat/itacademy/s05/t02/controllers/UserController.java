package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import cat.itacademy.s05.t02.repositories.PetRepository;
import cat.itacademy.s05.t02.services.PetService;
import cat.itacademy.s05.t02.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "User", description = "the User API")
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PetService petService;
	

	@Operation(summary = "User home page", description = "Home page for logged in users.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleUserWelcome() {
		return Mono.just(ResponseEntity.ok("You are now logged in, welcome!"));
	}
	
	/*
	@Operation(summary = "Create new pet", description = "Create a new user's pet ")
	@PostMapping("/create")
	public Mono<ResponseEntity<MyUser>> createNewPet(@RequestBody String newPet) {
		return userService.createNewPet(Mono.just(newPet))
				.flatMap(pet -> userService.saveNewPet(Mono.just(pet))
						.map(newGame -> ResponseEntity.status(HttpStatus.CREATED).body(newPet))
						.onErrorMap(e -> new IllegalArgumentException("Error saving new game. ")));
	}
	*/

}
