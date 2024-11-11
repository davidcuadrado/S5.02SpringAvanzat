package cat.itacademy.s05.t02.services;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.models.MyUser;
import cat.itacademy.s05.t02.repositories.MyUserRepository;

class MyUserDetailServiceTest {

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    @Mock
    private MyUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_withExistingUser_returnsUserDetails() {
        MyUser myUser = new MyUser();
        myUser.setUsername("testuser");
        myUser.setPassword("password123");
        myUser.setRole("USER");

        when(userRepository.findByUsername(anyString())).thenReturn(Mono.just(myUser));

        Mono<UserDetails> result = myUserDetailService.findByUsername("testuser");

        StepVerifier.create(result)
            .expectNextMatches(userDetails ->
                userDetails.getUsername().equals("testuser") &&
                userDetails.getPassword().equals("password123") &&
                userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
            )
            .verifyComplete();
    }

    @Test
    void findByUsername_withNonExistingUser_throwsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Mono.empty());

        Mono<UserDetails> result = myUserDetailService.findByUsername("nonexistent");

        StepVerifier.create(result)
            .expectError(UsernameNotFoundException.class)
            .verify();
    }

    @Test
    void findByUsernameMono_withExistingUser_returnsUserDetails() {
        MyUser myUser = new MyUser();
        myUser.setUsername("testuser");
        myUser.setPassword("password123");
        myUser.setRole("USER");

        when(userRepository.findByUsername(anyString())).thenReturn(Mono.just(myUser));

        Mono<UserDetails> result = myUserDetailService.findByUsernameMono(Mono.just("testuser"));

        StepVerifier.create(result)
            .expectNextMatches(userDetails ->
                userDetails.getUsername().equals("testuser") &&
                userDetails.getPassword().equals("password123") &&
                userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
            )
            .verifyComplete();
    }

    @Test
    void findByUsernameMono_withNonExistingUser_throwsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Mono.empty());

        Mono<UserDetails> result = myUserDetailService.findByUsernameMono(Mono.just("nonexistent"));

        StepVerifier.create(result)
            .expectError(UsernameNotFoundException.class)
            .verify();
    }

    @Test
    void getRoles_withNullRole_returnsUserRole() {
        MyUser myUser = new MyUser();
        myUser.setRole(null);

        String[] roles = myUserDetailService.getRoles(myUser);

        assertArrayEquals(new String[] { "USER" }, roles);
    }

    @Test
    void getRoles_withMultipleRoles_returnsRolesArray() {
        MyUser myUser = new MyUser();
        myUser.setRole("USER,ADMIN");

        String[] roles = myUserDetailService.getRoles(myUser);

        assertArrayEquals(new String[] { "USER", "ADMIN" }, roles);
    }
}
