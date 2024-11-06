package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.PetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	public Mono<Pet> createNewPetString(Mono<String> petName) {
		return petName.flatMap(pet -> petRepository.save(new Pet(pet)))
				.onErrorMap(e -> new DatabaseException("Error creating new pet. "));
	}
	
	public Mono<Pet> createNewPet(Mono<Pet> monoPet) {
		return monoPet.flatMap(pet -> petRepository.save(pet))
				.onErrorMap(e -> new DatabaseException("Error creating new pet"));
	}

	public Flux<Pet> getAllPets() {
		return petRepository.findAll().switchIfEmpty(Mono.error(new NotFoundException("No existing players to show. ")))
				.sort((pet1, pet2) -> pet1.getPetId().compareTo(pet2.getPetId()))
				.onErrorMap(e -> new DatabaseException("Error retrieving ranked players. "));
	}

	public Mono<Pet> deletePetById(Mono<String> just) {
		// TODO Auto-generated method stub
		return null;
	}

	public Mono<ResponseEntity<String>> saveNewPet(Mono<Pet> just) {
		// TODO Auto-generated method stub
		return null;
	}

	public Flux<Pet> createNewPet(Pet pet) {
		// TODO Auto-generated method stub
		return null;
	}

	public Mono<Pet> updatePet(Mono<String> id, Mono<String> petAction) {
		// TODO Auto-generated method stub
		return null;
	}

}