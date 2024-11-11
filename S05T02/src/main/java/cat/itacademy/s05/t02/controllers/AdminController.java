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

@Tag(name = "Admin", description = "the Admin API")
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private PetService petService;

	@Autowired
	private JwtService jwtService;

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Admin home page", description = "Home page for logged in admins.")
	@GetMapping("/home")
	public Mono<ResponseEntity<String>> handleAdminWelcome() {
		return Mono.just(ResponseEntity.ok("Welcome back, you are now logged in!"));
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

	@Operation(summary = "Get all user's pets", description = "Retrieve all existing pets from the user.")
	@GetMapping("/read")
	public Mono<ResponseEntity<Flux<Pet>>> getUserPets(String userId) {
		return petService.getPetsByUserId(Mono.just(userId)).collectList().flatMap(pets -> {
			if (pets.isEmpty()) {
				return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
			} else {
				return Mono.just(ResponseEntity.ok(Flux.fromIterable(pets)));
			}
		});
	}

	@Operation(summary = "Get a pet", description = "Retrieve an specific pet using pet's ID. ")
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Pet>> getUserSpecificPet(@PathVariable("id") String petId) {
		return petService.findPetById(Mono.just(petId)).map(pet -> ResponseEntity.ok(pet))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@Operation(summary = "Delete a pet", description = "Delete an existing pet by providing its pet ID.")
	@DeleteMapping("/{id}/delete")
	public Mono<ResponseEntity<String>> deleteGame(@PathVariable("id") String petId) {
		return petService.findPetById(Mono.just(petId)).flatMap(pet -> petService.deletePetById(Mono.just(petId)).then(
				Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pet " + petId + " deleted successfully"))))
				.switchIfEmpty(Mono.error(new NotFoundException("Pet with ID: " + petId + " not found")))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						"An unexpected error occurred when trying to delete a pet. Check the data input before trying again.")));
	}

	@Operation(summary = "Interact with a pet", description = "Interact with a pet using a specific action.")
	@PostMapping("/{id}/update")
	public Mono<ResponseEntity<String>> updatePet(@PathVariable("id") String petId, @RequestBody String petAction) {
		return petService.findPetById(Mono.just(petId))
				.flatMap(pet -> petService.nextPetAction(Mono.just(petId), Mono.just(petAction))
						.map(petUpdate -> ResponseEntity.status(HttpStatus.OK).body(pet.toString()))
						.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body("Pet with ID: " + petId + " not found"))))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						"An unexpected error occurred when trying to interact with a pet. Check the data input and try again.")));
	}

}
