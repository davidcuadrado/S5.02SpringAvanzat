package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Pet", description = "the Pet API")
@RestController
@RequestMapping("/pet")
public class PetController {

	@Autowired
	private PetService petService;

	@Operation(summary = "Get all pets", description = "Retrieve all existing pets. ")
	@GetMapping("/all")
	public Flux<Pet> getPets() {
		return petService.getAllPets().switchIfEmpty(Flux.error(new NotFoundException("No pets found.")));
	}

	@Operation(summary = "Create a new pet", description = "Create a new pet in the pet repository. ")
	@PostMapping("/new")
	public Mono<ResponseEntity<Pet>> createPet(@RequestBody Pet pet) {
		return petService.createNewPet(Mono.just(pet))
				.map(createdPet -> ResponseEntity.status(HttpStatus.CREATED).body(createdPet))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}
	
	@Operation(summary = "Interact with a pet", description = "Interact with a pet using an specific action. ")
	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<Pet>> updatePet(@PathVariable String id, @RequestBody String petAction) {
        return petService.updatePet(Mono.just(id), Mono.just(petAction))
                .map(updatedPet -> ResponseEntity.ok(updatedPet))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
	
	@Operation(summary = "Delete a pet", description = "Delete a pet from de repository. ")
	@DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<String>> deletePet(@PathVariable String petId) {
        return petService.deletePetById(Mono.just(petId))
        		.map(deletedPet -> ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Pet num." + petId + " deleted succesfully"))
				.switchIfEmpty(Mono.error(new NotFoundException("Game with ID: " + petId + " not found")));
    }

}
