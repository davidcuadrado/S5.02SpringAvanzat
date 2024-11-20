package cat.itacademy.s05.t02.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import cat.itacademy.s05.t02.repositories.PetRepository;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private MyUserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    /*
    @Test
    void createNewPet_withValidData_savesPetSuccessfully() {
        String petName = "Fluffy";
        String userId = "123";
        Pet savedPet = new Pet(petName, userId);

        when(petRepository.save(any(Pet.class))).thenReturn(Mono.just(savedPet));

        Mono<Pet> result = userService.createNewPet(Mono.just(petName), Mono.just(userId));

        StepVerifier.create(result)
            .expectNext(savedPet)
            .verifyComplete();
    }
    */
    /*
    @Test
    void createNewPet_withDatabaseError_throwsDatabaseException() {
        String petName = "Fluffy";
        String userId = "123";

        when(petRepository.save(any(Pet.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<Pet> result = userService.createNewPet(Mono.just(petName), Mono.just(userId));

        StepVerifier.create(result)
            .expectError(DatabaseException.class)
            .verify();
    }
    */
}
