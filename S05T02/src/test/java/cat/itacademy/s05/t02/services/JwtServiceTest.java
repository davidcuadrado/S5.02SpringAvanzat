package cat.itacademy.s05.t02.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
    }

    @Test
    public void testGenerateTokenAndValidate() {
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();

        Mono<String> tokenMono = jwtService.generateToken(Mono.just(userDetails));

        StepVerifier.create(tokenMono)
            .expectNextMatches(token -> jwtService.isTokenValid(token).block())
            .verifyComplete();
    }

    @Test
    public void testIsTokenValid_InvalidToken() {
        String invalidToken = "invalidToken";

        Mono<Boolean> result = jwtService.isTokenValid(invalidToken);

        StepVerifier.create(result)
            .expectNext(false)
            .verifyComplete();
    }
}
