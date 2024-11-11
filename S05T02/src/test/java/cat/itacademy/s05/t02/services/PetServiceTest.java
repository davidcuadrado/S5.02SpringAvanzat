package cat.itacademy.s05.t02.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.exceptions.DatabaseException;
import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.exceptions.PetActionException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.repositories.PetRepository;

class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewPet_withValidData_savesPetSuccessfully() {
        String petName = "Fluffy";
        String userId = "123";
        Pet newPet = new Pet(petName, userId);

        when(petRepository.save(any(Pet.class))).thenReturn(Mono.just(newPet));

        Mono<Pet> result = petService.createNewPet(Mono.just(petName), Mono.just(userId));

        StepVerifier.create(result)
            .expectNext(newPet)
            .verifyComplete();
    }

    @Test
    void createNewPet_withDatabaseError_throwsDatabaseException() {
        String petName = "Fluffy";
        String userId = "123";

        when(petRepository.save(any(Pet.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<Pet> result = petService.createNewPet(Mono.just(petName), Mono.just(userId));

        StepVerifier.create(result)
            .expectError(DatabaseException.class)
            .verify();
    }

    @Test
    void findPetById_withExistingPet_returnsPet() {
        String petId = "123";
        Pet pet = new Pet("Fluffy", "user123");

        when(petRepository.findById(petId)).thenReturn(Mono.just(pet));

        Mono<Pet> result = petService.findPetById(Mono.just(petId));

        StepVerifier.create(result)
            .expectNext(pet)
            .verifyComplete();
    }

    @Test
    void findPetById_withNonExistingPet_throwsNotFoundException() {
        String petId = "123";

        when(petRepository.findById(petId)).thenReturn(Mono.empty());

        Mono<Pet> result = petService.findPetById(Mono.just(petId));

        StepVerifier.create(result)
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void playWithPet_withSufficientEnergy_updatesPet() {
        String petId = "123";
        Pet pet = new Pet("Fluffy", "user123");
        pet.setEnergy(50);

        Pet updatedPet = new Pet("Fluffy", "user123");
        updatedPet.setEnergy(40); // Expected after play action

        when(petRepository.findById(petId)).thenReturn(Mono.just(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(Mono.just(updatedPet));

        Mono<Pet> result = petService.playWithPet(Mono.just(petId));

        StepVerifier.create(result)
            .expectNext(updatedPet)
            .verifyComplete();
    }

    @Test
    void playWithPet_withInsufficientEnergy_throwsPetActionException() {
        String petId = "123";
        Pet pet = new Pet("Fluffy", "user123");
        pet.setEnergy(5); // Insufficient energy for play

        when(petRepository.findById(petId)).thenReturn(Mono.just(pet));

        Mono<Pet> result = petService.playWithPet(Mono.just(petId));

        StepVerifier.create(result)
            .expectError(PetActionException.class)
            .verify();
    }

    @Test
    void getAllPets_withExistingPets_returnsSortedPets() {
        Pet pet1 = new Pet("Buddy", "user123");
        pet1.setPetId("1");
        Pet pet2 = new Pet("Fluffy", "user123");
        pet2.setPetId("2");

        when(petRepository.findAll()).thenReturn(Flux.just(pet2, pet1)); // Initial unsorted order

        Flux<Pet> result = petService.getAllPets();

        StepVerifier.create(result)
            .expectNextMatches(p -> p.getPetId().equals("1"))
            .expectNextMatches(p -> p.getPetId().equals("2"))
            .verifyComplete();
    }

    @Test
    void getAllPets_withNoPets_throwsNotFoundException() {
        when(petRepository.findAll()).thenReturn(Flux.empty());

        Flux<Pet> result = petService.getAllPets();

        StepVerifier.create(result)
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void deletePetById_withExistingPet_deletesPetSuccessfully() {
        String petId = "123";
        Pet pet = new Pet("Fluffy", "user123");

        when(petRepository.findById(petId)).thenReturn(Mono.just(pet));
        when(petRepository.delete(pet)).thenReturn(Mono.empty());

        Mono<Pet> result = petService.deletePetById(Mono.just(petId));

        StepVerifier.create(result)
            .expectNext(pet)
            .verifyComplete();
    }

    @Test
    void deletePetById_withNonExistingPet_throwsNotFoundException() {
        String petId = "123";

        when(petRepository.findById(petId)).thenReturn(Mono.empty());

        Mono<Pet> result = petService.deletePetById(Mono.just(petId));

        StepVerifier.create(result)
            .expectError(NotFoundException.class)
            .verify();
    }
}
