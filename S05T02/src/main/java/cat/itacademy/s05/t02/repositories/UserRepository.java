package cat.itacademy.s05.t02.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t02.models.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>{

}
