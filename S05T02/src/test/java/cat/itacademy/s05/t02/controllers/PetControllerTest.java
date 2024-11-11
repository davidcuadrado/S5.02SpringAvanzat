package cat.itacademy.s05.t02.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.PetService;

class PetControllerTest {

    @InjectMocks
    private PetController petController;

    @Mock
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPets_withExistingPets_returnsPets() {
        Pet pet1 = new Pet("pet1Name", "userId");
        Pet pet2 = new Pet("per2Name", "userId");

        when(petService.getAllPets()).thenReturn(Flux.just(pet1, pet2));

        Flux<ResponseEntity<Pet>> result = petController.getAllPets();

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok(pet1))
            .expectNext(ResponseEntity.ok(pet2))
            .verifyComplete();
    }

    @Test
    void getAllPets_withNoPets_returnsNotFound() {
        when(petService.getAllPets()).thenReturn(Flux.empty());

        Flux<ResponseEntity<Pet>> result = petController.getAllPets();

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
            .verifyComplete();
    }
}
