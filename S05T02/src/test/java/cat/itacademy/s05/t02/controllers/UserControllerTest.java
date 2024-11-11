package cat.itacademy.s05.t02.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.exceptions.NotFoundException;
import cat.itacademy.s05.t02.models.Pet;
import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.PetService;
import cat.itacademy.s05.t02.services.UserService;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleUserWelcome_returnsWelcomeMessage() {
        Mono<ResponseEntity<String>> result = userController.handleUserWelcome();

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok("You are now logged in, welcome!"))
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

        Mono<ResponseEntity<Pet>> response = userController.createNewPet(authHeader, newPetName);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.CREATED && resp.getBody().equals(pet))
                .verifyComplete();
    }

    @Test
    void getUserPets_withExistingPets_returnsPets() {
        String authHeader = "Bearer validToken";
        String userId = "123";
        Pet pet1 = new Pet("pet1Name", userId);
        Pet pet2 = new Pet("pet2Name", userId);

        when(jwtService.extractUserId(any())).thenReturn(Mono.just(userId));
        when(petService.getPetsByUserId(Mono.just(userId))).thenReturn(Flux.just(pet1, pet2));

        Mono<ResponseEntity<Flux<Pet>>> result = userController.getUserPets(authHeader);

        StepVerifier.create(result)
            .assertNext(response -> {
                assertEquals(HttpStatus.OK, response.getStatusCode());
                StepVerifier.create(response.getBody())
                    .expectNext(pet1, pet2)
                    .verifyComplete();
            })
            .verifyComplete();
    }


    @Test
    void getUserSpecificPet_withExistingPet_returnsPet() {
        String authHeader = "Bearer validToken";
        String petId = "123";
        String userId = "123";
        Pet pet = new Pet("pet1Name", userId);

        when(jwtService.extractUserId(any())).thenReturn(Mono.just(userId));
        when(petService.getPetByUserIdAndPetId(Mono.just(userId), Mono.just(petId))).thenReturn(Mono.just(pet));

        Mono<ResponseEntity<Pet>> result = userController.getUserSpecificPet(authHeader, petId);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok(pet))
            .verifyComplete();
    }

    @Test
    void deleteGame_withAuthorizedUser_deletesPet() {
        String authHeader = "Bearer validToken";
        String petId = "123";
        String userId = "123";
        Pet pet = new Pet("Fluffy", userId);

        when(jwtService.extractUserId(any())).thenReturn(Mono.just(userId));
        when(petService.findPetById(Mono.just(petId))).thenReturn(Mono.just(pet));
        when(petService.deletePetById(Mono.just(petId))).thenReturn(Mono.empty());

        Mono<ResponseEntity<String>> result = userController.deleteGame(authHeader, petId);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pet " + petId + " deleted successfully"))
            .verifyComplete();
    }

    @Test
    void updatePet_withAuthorizedUser_updatesPet() {
        String authHeader = "Bearer validToken";
        String petId = "123";
        String userId = "345";
        String petAction = "play";
        Pet pet = new Pet("testPetName", userId);

        when(jwtService.extractUserId(any())).thenReturn(Mono.just(userId));
        when(petService.findPetById(Mono.just(petId))).thenReturn(Mono.just(pet));
        when(petService.nextPetAction(Mono.just(petId), Mono.just(petAction))).thenReturn(Mono.just(pet));

        Mono<ResponseEntity<String>> result = userController.updatePet(authHeader, petId, petAction);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.OK).body(pet.toString()))
            .verifyComplete();
    }

    @Test
    void updatePet_withUnauthorizedUser_returnsForbidden() {
        String authHeader = "Bearer validToken";
        String petId = "123";
        String userId = "123";
        String unauthorizedUserId = "456";
        String petAction = "play";
        Pet pet = new Pet("Fluffy", unauthorizedUserId);

        when(jwtService.extractUserId(any())).thenReturn(Mono.just(userId));
        when(petService.findPetById(Mono.just(petId))).thenReturn(Mono.just(pet));

        Mono<ResponseEntity<String>> result = userController.updatePet(authHeader, petId, petAction);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You are not authorized to interact with this pet."))
            .verifyComplete();
    }
}
