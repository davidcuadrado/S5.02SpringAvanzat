package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.PetService;
import cat.itacademy.s05.t02.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "User", description = "the User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PetService petService;

	@Autowired
	private JwtService jwtService;

	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "User home page", description = "Home page for logged in users.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleUserWelcome() {
		return Mono.just(ResponseEntity.ok("You are now logged in, welcome!"));
	}

	@Operation(summary = "Delete a pet", description = "Delete an existing pet introducing its pet ID. ")
	@DeleteMapping("/{id}/delete")
	public Mono<ResponseEntity<String>> deleteGame(@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") String petId) {
		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt)
				.flatMap(searchByPetId -> petService.findPetById(Mono.just(petId)).flatMap(pet -> {
					if (pet.getUserId().equals(jwtService.extractUserId(jwt))) {
						return petService.deletePetById(Mono.just(petId)).then(Mono.just(ResponseEntity
								.status(HttpStatus.NO_CONTENT).body("Pet " + petId + " deleted successfully")));
					} else {

						return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
								.body("You are not authorized to delete this pet."));
					}
				})

						.switchIfEmpty(Mono.error(new NotFoundException("Pet with ID: " + petId + " not found"))));
	}
	
	@Operation(summary = "Create new pet", description = "Create a new user's pet ")
	@PostMapping("/create")
	public Mono<ResponseEntity<Pet>> createNewPet(@RequestHeader("Authorization") String authHeader,
			@RequestBody String newPetName) {
		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt)
				.flatMap(userId -> userService.createNewPet(Mono.just(newPetName), Mono.just(userId)))
				.map(createdPet -> ResponseEntity.status(HttpStatus.CREATED).body(createdPet))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
	}

	@Operation(summary = "Get all user pets", description = "Retrieve all user existing pets. ")
	@GetMapping("/pets")
	public Flux<Pet> getUserPets() {
		return petService.getAllPets().switchIfEmpty(Flux.error(new NotFoundException("No pets found for the user.")));
	}

}
