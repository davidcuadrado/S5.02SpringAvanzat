package cat.itacademy.s05.t02.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@Tag(name = "Pet", description = "the Pet API")
@RestController
@RequestMapping("/pet")
public class PetController {

	@Autowired
	private PetService petService;

	@Operation(summary = "Get all pets", description = "Retrieve all existing pets. ")
	@GetMapping("/all")
	public Flux<ResponseEntity<Pet>> getAllPets() {
	    return petService.getAllPets()
	            .map(pet -> ResponseEntity.ok(pet))
	            .switchIfEmpty(Flux.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
	}
	
}
