package cat.itacademy.s05.t02.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;

class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_withValidUser_returnsSavedUser() {
        MyUser user = new MyUser("testUsername", "testPassword");

        MyUser savedUser = new MyUser("testUsername2", "testPassword2");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword123");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(Mono.just(savedUser));

        Mono<ResponseEntity<MyUser>> result = registrationController.createUser(user);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok(savedUser))
            .verifyComplete();
    }

    @Test
    void createUser_withRepositoryError_returnsBadRequest() {
    	MyUser user = new MyUser("testUsername", "testPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword123");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<MyUser>> result = registrationController.createUser(user);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.badRequest().build())
            .verifyComplete();
    }
}
