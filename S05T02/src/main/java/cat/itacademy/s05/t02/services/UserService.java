package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import cat.itacademy.s05.t02.repositories.PetRepository;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private MyUserRepository userRepository;

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

}
