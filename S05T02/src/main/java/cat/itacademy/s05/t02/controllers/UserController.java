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

	@Operation(summary = "Create new pet", description = "Create a new user's pet ")
	@PostMapping("/create")
	public Mono<ResponseEntity<Pet>> createPet(@RequestBody Pet pet, @RequestHeader("Authorization") String token) {
	    return jwtService.extractUserId(token)
	        .flatMap(userId -> {
	            System.out.println("Extracted userId: " + userId);
	            return userService.createNewPet(Mono.just(pet), Mono.just(userId))
	                .map(savedPet -> ResponseEntity.status(HttpStatus.CREATED).body(savedPet));
	        })
	        .onErrorResume(e -> {
	            System.err.println("Error creating pet: " + e.getMessage());
	            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	        });
	}


	@Operation(summary = "Get all user's pets", description = "Retrieve all existing pets from the user. ")
	@GetMapping("/read")
	public Mono<ResponseEntity<Flux<Pet>>> getUserPets(@RequestHeader("Authorization") String authHeader) {
		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt).flatMapMany(userId -> petService.getPetsByUserId(Mono.just(userId)))
				.collectList().flatMap(pets -> {
					if (pets.isEmpty()) {
						return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
					} else {
						return Mono.just(ResponseEntity.ok(Flux.fromIterable(pets)));
					}
				});
	}

	@Operation(summary = "Get a pet from a user", description = "Retrieve an specific pet from a user. ")
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Pet>> getUserSpecificPet(@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") String petId) {

		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt)
				.flatMap(userId -> petService.getPetByUserIdAndPetId(Mono.just(userId), Mono.just(petId)))
				.map(pet -> ResponseEntity.ok(pet)).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@Operation(summary = "Delete a pet", description = "Delete an existing pet introducing its pet ID. ")
	@DeleteMapping("/{id}/delete")
	public Mono<ResponseEntity<String>> deletePet(@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") String petId) {
		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt).flatMap(userId -> petService.findPetById(Mono.just(petId)).flatMap(pet -> {
			if (pet.getUserId().equals(userId)) {
				return petService.deletePetById(Mono.just(petId)).then(Mono.just(
						ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pet " + petId + " deleted successfully. ")));
			} else {
				return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You are not authorized to delete this pet. "));
			}
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet with ID: " + petId + " not found"))))
				.onErrorResume(NotFoundException.class,
						e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage())))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("An unexpected error occurred when trying to delete the pet. Please try again.")));
	}

	@Operation(summary = "Interact with a pet", description = "Interact with a pet using an specific action. ")
	@PostMapping("/{id}/update")
	public Mono<ResponseEntity<String>> updatePet(@RequestHeader("Authorization") String authHeader,
			@PathVariable("id") String petId, @RequestBody String petAction) {
		String jwt = authHeader.replace("Bearer ", "");
		return jwtService.extractUserId(jwt).flatMap(userId -> petService.findPetById(Mono.just(petId)).flatMap(pet -> {
			if (pet.getUserId().equals(userId)) {
				return petService.nextPetAction(Mono.just(petId), Mono.just(petAction))
						.map(petUpdate -> ResponseEntity.status(HttpStatus.OK).body(pet.toString()));
			} else {
				return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You are not authorized to interact with this pet."));
			}
		}).switchIfEmpty(
				Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet with ID: " + petId + " not found"))))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You are not authorized to interact with this pet.")));

	}

}
