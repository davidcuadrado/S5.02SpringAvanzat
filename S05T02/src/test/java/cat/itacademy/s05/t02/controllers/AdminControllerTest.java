package cat.itacademy.s05.t02.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.PetService;
import cat.itacademy.s05.t02.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void handleAdminWelcome_ShouldReturnWelcomeMessage() {
        Mono<ResponseEntity<String>> response = adminController.handleAdminWelcome();
        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok("Welcome back, you are now logged in!"))
                .verifyComplete();
    }

    @Test
    void createNewPet_ShouldReturnCreatedPet() {
        String authHeader = "Bearer testToken";
        String newPetName = "testNewPetName";
        String userId = "123";
        Pet pet = new Pet(newPetName, userId);

        when(jwtService.extractUserId("testToken")).thenReturn(Mono.just(userId));
        when(userService.createNewPet(any(Mono.class), any(Mono.class))).thenReturn(Mono.just(pet));

        Mono<ResponseEntity<Pet>> response = adminController.createNewPet(authHeader, newPetName);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.CREATED && resp.getBody().equals(pet))
                .verifyComplete();
    }

    @Test
    void getUserPets_ShouldReturnUserPets() {
        String userId = "123";
        Pet pet1 = new Pet("pet1Name", userId);
        Pet pet2 = new Pet("pet2Name", userId);

        when(petService.getPetsByUserId(any(Mono.class))).thenReturn(Flux.just(pet1, pet2));


        Mono<ResponseEntity<Flux<Pet>>> response = adminController.getUserPets(userId);

        StepVerifier.create(response)
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertEquals(2, resp.getBody().count().block().intValue());
                })
                .verifyComplete();
    }

    @Test
    void getUserSpecificPet_ShouldReturnPet() {
        String petId = "456";
        String userId = "123";
        Pet pet = new Pet(petId, userId);

        when(petService.findPetById(any(Mono.class))).thenReturn(Mono.just(pet));


        Mono<ResponseEntity<Pet>> response = adminController.getUserSpecificPet(petId);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(pet))
                .verifyComplete();
    }

    @Test
    void deletePet_ShouldDeletePetSuccessfully() {
    	String petId = "456";
        String userId = "123";
        Pet pet = new Pet(petId, userId);
        
        when(petService.findPetById(any(Mono.class))).thenReturn(Mono.just(pet));
        when(petService.deletePetById(any(Mono.class))).thenReturn(Mono.empty());


        Mono<ResponseEntity<String>> response = adminController.deleteGame(petId);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pet " + petId + " deleted successfully"))
                .verifyComplete();
    }

    @Test
    void updatePet_ShouldReturnUpdatedPet() {
        String petId = "456";
        String petAction = "play";
        Pet pet = new Pet(petId, petAction);

        when(petService.findPetById(any(Mono.class))).thenReturn(Mono.just(pet));
        when(petService.nextPetAction(any(Mono.class), any(Mono.class))).thenReturn(Mono.just(pet));

        Mono<ResponseEntity<String>> response = adminController.updatePet(petId, petAction);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.OK && resp.getBody().contains(pet.toString()))
                .verifyComplete();
    }

}
