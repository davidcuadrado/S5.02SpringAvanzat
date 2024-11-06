package cat.itacademy.s05.t02.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MyUserDetailServiceTest {

    @Mock
    private MyUserRepository myUserRepository;

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername_UserExists() {
        MyUser user = new MyUser("testuser", "encodedpassword");

        when(myUserRepository.findByUsername(anyString())).thenReturn(Mono.just(user));

        Mono<UserDetails> result = myUserDetailService.findByUsername("testuser");

        StepVerifier.create(result)
            .expectNextMatches(userDetails -> userDetails.getUsername().equals("testuser"))
            .verifyComplete();
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        when(myUserRepository.findByUsername(anyString())).thenReturn(Mono.empty());

        Mono<UserDetails> result = myUserDetailService.findByUsername("nonexistent");

        StepVerifier.create(result)
            .expectError(UsernameNotFoundException.class)
            .verify();
    }
}

