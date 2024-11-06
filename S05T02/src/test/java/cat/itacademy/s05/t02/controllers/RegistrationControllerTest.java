package cat.itacademy.s05.t02.controllers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cat.itacademy.s05.t02.controllers.RegistrationController;
import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

public class RegistrationControllerTest {

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        
        MyUser user = new MyUser("testuser", "testpassword");
        user.setUsername("testuser");
        user.setPassword("testpassword");

        
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(Mono.just(user));

        
        Mono<ResponseEntity<MyUser>> response = registrationController.createUser(user);
        response.subscribe(result -> {
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals("testuser", result.getBody().getUsername());
        });
    }
}
