package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
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

	public Mono<Pet> deletePetById(Mono<String> monoPetId) {
		return monoPetId.flatMap(id -> petRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + monoPetId + " not found.")))
				.flatMap(existingGame -> petRepository.delete(existingGame).then(Mono.just(existingGame)));
	}

	public Mono<Pet> updatePet(Mono<String> petIdMono, Mono<String> petActionMono) {
		return petIdMono.flatMap(id -> petRepository.findById(id).zipWith(petActionMono, (existingPet, action) -> {
			// Bussiness logic
			// i.e.: existingPet.setAction(action);
			return existingPet;
		}).flatMap(petRepository::save));
	}

}