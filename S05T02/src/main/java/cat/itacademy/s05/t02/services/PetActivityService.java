package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.models.PetMood;
import cat.itacademy.s05.t02.repositories.PetRepository;
import reactor.core.publisher.Mono;

@Service
public class PetActivityService {

	@Autowired
	private PetRepository petRepository;

	public Mono<Pet> feedPet(String petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setHunger(Math.max(pet.getHunger() - 20, 0));
			pet.setHappiness(pet.getHappiness() + 10);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> playWithPet(String petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			if (pet.getEnergy() < 10) {
				return Mono.error(new DatabaseException("Pet is too tired to play"));
			}
			pet.setEnergy(pet.getEnergy() - 10);
			pet.setHappiness(pet.getHappiness() + 15);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> addAccessory(String petId, String accessory) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.getSpecialTreats().add(accessory);
			pet.setHappiness(pet.getHappiness() + 5);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> changeEnvironment(String petId, String environment) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setEnvironment(environment);
			pet.setHappiness(pet.getHappiness() + 5);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> improveMood(String petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setCurrentMood(PetMood.HAPPY);
			pet.setHappiness(pet.getHappiness() + 10);
			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

}
