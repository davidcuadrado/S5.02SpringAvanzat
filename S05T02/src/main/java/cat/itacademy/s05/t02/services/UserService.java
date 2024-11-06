package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.models.MyUser;
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

	@Autowired
	private JwtService jwtService;

	public Mono<Pet> createNewPet(Mono<String> petName) {
		return petName.flatMap(pet -> petRepository.save(new Pet(pet)))
				.onErrorMap(e -> new DatabaseException("Error creating new pet. "));
	}

}
