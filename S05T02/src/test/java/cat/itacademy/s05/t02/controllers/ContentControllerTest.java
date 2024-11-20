package cat.itacademy.s05.t02.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;
import cat.itacademy.s05.t02.token.LoginForm;

class ContentControllerTest {

    @InjectMocks
    private ContentController contentController;

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private MyUserDetailService myUserDetailService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleWelcome_returnsWelcomeMessage() {
        Mono<ResponseEntity<String>> result = contentController.handleWelcome();

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok("You are in the home site"))
            .verifyComplete();
    }
    
    /*
    @Test
    void handleLogin_returnsLoginMessage() {
        Mono<ResponseEntity<String>> result = contentController.handleLogin(null);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok()).body(token)
            .verifyComplete();
    }
    */
    /*
    @Test
    void authenticateAndGetToken_withValidCredentials_returnsToken() {
        LoginForm loginForm = new LoginForm("validUser", "validPassword");
        String generatedToken = "mockedJwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(Mono.just(authentication));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(myUserDetailService.findByUsername(loginForm.username()))
            .thenReturn(Mono.just(userDetails));
        when(jwtService.generateToken(any(Mono.class)))
            .thenReturn(Mono.just(generatedToken));

        Mono<ResponseEntity<String>> result = contentController.authenticateAndGetToken(loginForm);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.ok(generatedToken))
            .verifyComplete();
    }


    @Test
    void authenticateAndGetToken_withInvalidCredentials_returnsUnauthorized() {
        LoginForm loginForm = new LoginForm("invalidUser", "invalidPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(Mono.empty());

        Mono<ResponseEntity<String>> result = contentController.authenticateAndGetToken(loginForm);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication process error. "))
            .verifyComplete();
    }

    @Test
    void authenticateAndGetToken_withValidCredentialsButUserNotFound_returnsUnauthorized() {
        LoginForm loginForm = new LoginForm("validUser", "validPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(Mono.just(authentication));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(myUserDetailService.findByUsername(loginForm.username()))
            .thenReturn(Mono.error(new UsernameNotFoundException("Invalid credentials")));

        Mono<ResponseEntity<String>> result = contentController.authenticateAndGetToken(loginForm);

        StepVerifier.create(result)
            .expectNext(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication process error. "))
            .verifyComplete();
    }
    */
}
