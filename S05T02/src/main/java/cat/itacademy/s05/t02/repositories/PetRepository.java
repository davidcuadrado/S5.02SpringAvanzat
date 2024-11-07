package cat.itacademy.s05.t02.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t02.models.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PetRepository extends ReactiveMongoRepository<Pet, String>{
	
	Flux<Pet> findAllByUserId(String ownerId);

	Mono<Pet> findByUserIdAndPetId(String userId, String petId);

}
