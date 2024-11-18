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
			return petRepository.save(pet);
		});
	}

}
