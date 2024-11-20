package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.PetRepository;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private PetRepository petRepository;

	public Mono<Pet> createNewPet(Mono<Pet> petMono, Mono<String> userIdMono) {
		return Mono.zip(petMono, userIdMono).flatMap(tuple -> {
			Pet pet = tuple.getT1();
			String userId = tuple.getT2();
			pet.setUserId(userId);
			
			 if (pet.getHappiness() == 0) pet.setHappiness(100);
	            if (pet.getEnergy() == 0) pet.setEnergy(100);
	            if (pet.getHunger() == 0) pet.setHunger(50);
	            if (pet.getHygiene() == 0) pet.setHygiene(100);
	            if (pet.getHealth() == 0) pet.setHealth(100);
	            
			return petRepository.save(pet);
		});
	}

}
