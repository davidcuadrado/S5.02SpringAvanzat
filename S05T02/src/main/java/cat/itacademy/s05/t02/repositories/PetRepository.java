package cat.itacademy.s05.t02.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t02.models.Pet;

@Repository
public interface PetRepository extends ReactiveMongoRepository<Pet, String>{

}