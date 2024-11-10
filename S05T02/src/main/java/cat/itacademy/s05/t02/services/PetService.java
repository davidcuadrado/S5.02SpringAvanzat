package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.models.PetMood;
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

	public Flux<Pet> getPetsByUserId(Mono<String> monoUserId) {
		return monoUserId.flatMapMany(id -> petRepository.findAllByUserId(id)).switchIfEmpty(Flux.empty());
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

	public Mono<Pet> getPetByUserIdAndPetId(Mono<String> monoUserId, Mono<String> monoPetId) {
		return monoUserId.zipWith(monoPetId).flatMap(tuple -> {
			String userId = tuple.getT1();
			String petId = tuple.getT2();
			return petRepository.findByUserIdAndPetId(userId, petId);
		});
	}

	/*
	 * return gameId.flatMap(id -> gameRepository.findById(id).flatMap(game ->
	 * playType.flatMap(type -> { switch (type.toLowerCase()) {
	 * 
	 * return monoPetId.flatMap(petId -> petRepository.findById(petId).flatMap(pet
	 * -> petAction.flatMap(action -> { switch (action.toLowerCase())
	 */

	public Mono<Pet> nextPlayType(Mono<String> monoPetId, Mono<String> monoPetAction) {
		return monoPetId.flatMap(id -> petRepository.findById(id).flatMap(pet -> monoPetAction.flatMap(petAction -> {
			switch (petAction.toLowerCase()) {
			case "feed":
				return feedPet(monoPetId).then(petRepository.save(pet));

			case "play":
				return playWithPet(monoPetId).then(petRepository.save(pet));

			case "environment":
				return changeEnvironment(monoPetId, "requested environment").then(petRepository.save(pet));
			case "cheer":
				return cheerPet(monoPetId).then(petRepository.save(pet));

			default:
				return Mono.error(
						new IllegalArgumentException("Invalid pet action. Try introducing a proper pet action."));
			}
		}))).switchIfEmpty(Mono.error(new NotFoundException("Pet not found.")));
	}

	public Mono<Pet> feedPet(Mono<String> petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setHunger(Math.max(pet.getHunger() - 20, 0));
			pet.setHappiness(pet.getHappiness() + 10);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> playWithPet(Mono<String> petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			if (pet.getEnergy() < 10) {
				return Mono.error(new DatabaseException("Pet is too tired to play"));
			}
			pet.setEnergy(pet.getEnergy() - 10);
			pet.setHappiness(pet.getHappiness() + 15);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> changeEnvironment(Mono<String> petId, String environment) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setEnvironment(environment);
			pet.setHappiness(pet.getHappiness() + 5);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> cheerPet(Mono<String> petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setCurrentMood(PetMood.HAPPY);
			pet.setHappiness(pet.getHappiness() + 10);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> addAccessory(Mono<String> petId, Mono<String> accessory) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> accessory.map(acc -> {
			pet.getSpecialTreats().add(acc);
			pet.setHappiness(pet.getHappiness() + 5);
			return pet;
		})).flatMap(petRepository::save)).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

}
