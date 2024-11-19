package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.exceptions.PetActionException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.models.PetEnvironment;
import cat.itacademy.s05.t02.models.PetMood;
import cat.itacademy.s05.t02.repositories.PetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	private static final Random random = new Random();

	public Mono<Pet> createNewPet(Mono<Pet> petMono, Mono<String> userIdMono) {
		return Mono.zip(petMono, userIdMono).flatMap(tuple -> {
			Pet pet = tuple.getT1();
			String userId = tuple.getT2();
			pet.setUserId(userId);
			return petRepository.save(pet);
		});
	}


	public Mono<Pet> findPetById(Mono<String> monoPetId) {
		return monoPetId.flatMap(petId -> petRepository.findById(petId)
				.switchIfEmpty(Mono.error(new NotFoundException("Pet not found"))));
	}

	public Flux<Pet> getPetsByUserId(Mono<String> monoUserId) {
		return monoUserId.flatMapMany(id -> petRepository.findAllByUserId(id))
				.switchIfEmpty(Mono.error(new NotFoundException("No pets found for that user.")));
	}

	public Flux<Pet> getAllPets() {
		return petRepository.findAll().sort((pet1, pet2) -> pet1.getPetId().compareTo(pet2.getPetId()))
				.switchIfEmpty(Mono.error(new NotFoundException("No existing pets to show. ")));
	}

	public Mono<Pet> deletePetById(Mono<String> monoPetId) {
		return monoPetId.flatMap(id -> petRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Pet ID: " + monoPetId + " not found.")))
				.flatMap(existingGame -> petRepository.delete(existingGame).then(Mono.just(existingGame)));
	}

	public Mono<Pet> getPetByUserIdAndPetId(Mono<String> monoUserId, Mono<String> monoPetId) {
		return monoUserId.zipWith(monoPetId).flatMap(tuple -> {
			String userId = tuple.getT1();
			String petId = tuple.getT2();
			return petRepository.findByUserIdAndPetId(userId, petId);
		});
	}

	public Mono<Pet> nextPetAction(Mono<String> monoPetId, Mono<String> monoPetAction) {
		return monoPetId.flatMap(id -> petRepository.findById(id).flatMap(pet -> monoPetAction.flatMap(petAction -> {
			switch (petAction.toLowerCase()) {
			case "feed":
				return feedPet(monoPetId);

			case "play":
				return playWithPet(monoPetId);

			case "environment":
				return changeEnvironment(monoPetId, Mono.just(PetEnvironment.valueOf(petAction)));

			case "cheer":
				return cheerPet(monoPetId);

			case "accessory":
				return addAccessory(monoPetId, Mono.just("requested accessory"));

			case "sleep":
				return putPetToSleep(monoPetId);

			case "clean":
				return cleanPet(monoPetId);

			case "adventure":
				return takePetOnAdventure(monoPetId);

			case "check":
				return checkAndRestoreHealth(monoPetId);

			case "special":
				return giveSpecialTreat(monoPetId, Mono.just("request special treat"));

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
			pet.setEnergy(Math.min(pet.getEnergy() + 5, 100));
			pet.setHygiene(Math.max(pet.getHygiene() - 5, 0));

			if (random.nextDouble() < 0.1) {
				pet.setHealth(Math.max(pet.getHealth() - 30, 0));
				pet.setHappiness(Math.max(pet.getHappiness() - 20, 0));
			}

			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> playWithPet(Mono<String> petIdMono) {
		return petIdMono.flatMap(petId -> petRepository.findById(petId).flatMap(pet -> {
			if (pet.getEnergy() < 10) {
				return Mono.error(new PetActionException("Pet is too tired to play"));
			}
			pet.setEnergy(pet.getEnergy() - 10);
			pet.setHappiness(pet.getHappiness() + 15);
			pet.setHygiene(Math.max(pet.getHygiene() - 10, 0));

			if (random.nextDouble() < 0.2) {
				pet.setHealth(Math.max(pet.getHealth() - 25, 0));
				pet.setEnergy(Math.max(pet.getEnergy() - 15, 0));
			}

			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found"))));
	}

	public Mono<Pet> changeEnvironment(Mono<String> petId, Mono<PetEnvironment> environment) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> environment.map(env -> {
			pet.setEnvironment(env);
			pet.setHappiness(pet.getHappiness() + 5);
			pet.setEnergy(Math.max(pet.getEnergy() - 5, 0));

			if (random.nextDouble() < 0.15) {
				pet.setHappiness(Math.max(pet.getHappiness() - 20, 0));
				pet.setEnergy(Math.max(pet.getEnergy() - 10, 0));
			}

			return pet;
		})).flatMap(petRepository::save)).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> cheerPet(Mono<String> petId) {
		return petRepository.findById(petId).flatMap(pet -> {
			pet.setCurrentMood(PetMood.HAPPY);
			pet.setHappiness(pet.getHappiness() + 10);
			pet.setEnergy(Math.max(pet.getEnergy() - 5, 0));

			if (random.nextDouble() < 0.05) {
				pet.setCurrentMood(PetMood.ANXIOUS);
				pet.setHappiness(Math.max(pet.getHappiness() - 15, 0));
			}

			return petRepository.save(pet);
		}).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> addAccessory(Mono<String> petId, Mono<String> accessory) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> accessory.map(acc -> {
			pet.getSpecialTreats().add(acc);
			pet.setHappiness(pet.getHappiness() + 5);
			pet.setHygiene(Math.max(pet.getHygiene() - 5, 0));

			if (random.nextDouble() < 0.1) {
				pet.setHappiness(Math.max(pet.getHappiness() - 10, 0));
			}

			return pet;
		})).flatMap(petRepository::save)).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> putPetToSleep(Mono<String> petId) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> {
			pet.setEnergy(Math.min(pet.getEnergy() + 50, 100));
			pet.setHappiness(pet.getHappiness() + 5);
			pet.setHunger(Math.min(pet.getHunger() + 10, 100));
			pet.setCurrentMood(PetMood.CALM);

			if (random.nextDouble() < 0.05) {
				pet.setHappiness(Math.max(pet.getHappiness() - 15, 0));
				pet.setEnergy(Math.max(pet.getEnergy() - 10, 0));
			}

			return petRepository.save(pet);
		})).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> cleanPet(Mono<String> petId) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> {
			pet.setHygiene(Math.min(pet.getHygiene() + 30, 100));
			pet.setHappiness(pet.getHappiness() + 5);
			pet.setEnergy(Math.max(pet.getEnergy() - 5, 0));

			if (random.nextDouble() < 0.1) {
				pet.setHappiness(Math.max(pet.getHappiness() - 10, 0));
			}

			return petRepository.save(pet);
		})).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> takePetOnAdventure(Mono<String> petId) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> {
			if (pet.getEnergy() < 20) {
				return Mono.error(new DatabaseException("Pet is too tired for an adventure"));
			}
			pet.setEnergy(pet.getEnergy() - 20);
			pet.setHappiness(pet.getHappiness() + 20);
			pet.setHygiene(Math.max(pet.getHygiene() - 15, 0));

			if (random.nextDouble() < 0.2) {
				pet.setHappiness(Math.max(pet.getHappiness() - 25, 0));
				pet.setEnergy(Math.max(pet.getEnergy() - 20, 0));
			}

			return petRepository.save(pet);
		})).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> checkAndRestoreHealth(Mono<String> petId) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> {
			if (pet.getHealth() < 50) {
				pet.setHealth(100);
				pet.setHappiness(pet.getHappiness() + 10);
				pet.setEnergy(Math.max(pet.getEnergy() - 10, 0));

				if (random.nextDouble() < 0.05) {
					pet.setHealth(Math.max(pet.getHealth() - 20, 0));
					pet.setHappiness(Math.max(pet.getHappiness() - 15, 0));
				}
			}
			return petRepository.save(pet);
		})).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

	public Mono<Pet> giveSpecialTreat(Mono<String> petId, Mono<String> treat) {
		return petId.flatMap(id -> petRepository.findById(id).flatMap(pet -> treat.map(t -> {
			pet.getSpecialTreats().add(t);
			pet.setHappiness(pet.getHappiness() + 10);
			pet.setEnergy(Math.min(pet.getEnergy() + 10, 100));
			pet.setHygiene(Math.max(pet.getHygiene() - 5, 0));

			if (random.nextDouble() < 0.1) {
				pet.setHappiness(Math.max(pet.getHappiness() - 15, 0));
				pet.setHealth(Math.max(pet.getHealth() - 20, 0));
			}

			return pet;
		})).flatMap(petRepository::save)).switchIfEmpty(Mono.error(new NotFoundException("Pet not found")));
	}

}
