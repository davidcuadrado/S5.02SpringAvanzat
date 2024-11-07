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

	public Mono<Pet> createNewPet(Mono<String> petName, Mono<String> monoUserId) {
		return Mono.zip(petName, monoUserId).flatMap(tuple -> {
			String name = tuple.getT1();
			String userId = tuple.getT2();

			Pet newPet = new Pet(name, userId);

			return petRepository.save(newPet);
		}).onErrorMap(e -> new DatabaseException("Error creating new pet."));
	}

	public Mono<Pet> createNewPet(Mono<Pet> monoPet) {
		return monoPet.flatMap(pet -> petRepository.save(pet))
				.onErrorMap(e -> new DatabaseException("Error creating new pet"));
	}
	
	public Mono<Pet> findPetById(Mono<String> monoPetId) {
		return monoPetId.flatMap(petId -> petRepository.findById(petId));
	}
	
	public Flux<Pet> getPetsByUserId(Mono<String> userId) {
	    return userId.flatMapMany(id -> petRepository.findAllByUserId(id))
	            .switchIfEmpty(Flux.empty());
	}

	public Flux<Pet> getAllPets() {
		return petRepository.findAll().switchIfEmpty(Mono.error(new NotFoundException("No existing pets to show. ")))
				.sort((pet1, pet2) -> pet1.getPetId().compareTo(pet2.getPetId()))
				.onErrorMap(e -> new DatabaseException("Error retrieving pets. "));
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