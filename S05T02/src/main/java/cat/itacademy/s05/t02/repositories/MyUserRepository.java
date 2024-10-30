package cat.itacademy.s05.t02.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t02.models.MyUser;
import reactor.core.publisher.Mono;

@Repository
public interface MyUserRepository extends ReactiveMongoRepository<MyUser, String>{

	Mono<MyUser> findByUsername(String username);

}
