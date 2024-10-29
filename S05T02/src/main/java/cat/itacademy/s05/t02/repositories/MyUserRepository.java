package cat.itacademy.s05.t02.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t02.models.MyUser;

@Repository
public interface MyUserRepository extends ReactiveMongoRepository<MyUser, String>{

	Optional<MyUser> findByUsername(String username);

}
