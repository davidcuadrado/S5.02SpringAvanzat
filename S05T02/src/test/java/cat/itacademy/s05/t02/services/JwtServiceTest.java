package cat.itacademy.s05.t02.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class JwtServiceTest {

	@InjectMocks
	private JwtService jwtService;

	@Mock
	private UserDetails userDetails;

	private String validToken;
	private String expiredToken;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		when(userDetails.getUsername()).thenReturn("testUser");

		validToken = Jwts.builder().subject("testUser").issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(1800000))).signWith(jwtService.generateKey()).compact();

		expiredToken = Jwts.builder().subject("testUser").issuedAt(Date.from(Instant.now().minusSeconds(3600)))
				.expiration(Date.from(Instant.now().minusSeconds(1800))).signWith(jwtService.generateKey()).compact();
	}

	@Test
	void generateToken_withValidUserDetails_returnsToken() {
		when(userDetails.getUsername()).thenReturn("testUser");

		Mono<String> result = jwtService.generateToken(Mono.just(userDetails));

		StepVerifier.create(result).assertNext(token -> {
			Claims claims = Jwts.parser().verifyWith(jwtService.generateKey()).build().parseSignedClaims(token)
					.getPayload();

			String username = claims.getSubject();

			assertEquals("testUser", username);
		}).verifyComplete();
	}

	@Test
	void validateAndExtractUsername_withValidToken_returnsUsername() {
		Mono<String> result = jwtService.validateAndExtractUsername(validToken);

		StepVerifier.create(result).expectNext("testUser").verifyComplete();
	}

	@Test
	void validateAndExtractUsername_withInvalidToken_throwsException() {
		String invalidToken = validToken + "invalid";

		Mono<String> result = jwtService.validateAndExtractUsername(invalidToken);

		StepVerifier.create(result).expectError(SignatureException.class).verify();
	}

	@Test
	void extractUsername_withValidToken_returnsUsername() {
		Mono<String> result = jwtService.extractUsername(validToken);

		StepVerifier.create(result).expectNext("testUser").verifyComplete();
	}

	@Test
	void extractUsername_withInvalidToken_throwsException() {
		String invalidToken = validToken + "invalid";

		Mono<String> result = jwtService.extractUsername(invalidToken);

		StepVerifier.create(result).expectError(IllegalArgumentException.class).verify();
	}

	@Test
	void isTokenValid_withValidToken_returnsTrue() {
		Mono<Boolean> result = jwtService.isTokenValid(validToken);

		StepVerifier.create(result).expectNext(true).verifyComplete();
	}

	@Test
	void isTokenValid_withExpiredToken_returnsFalse() {
		Mono<Boolean> result = jwtService.isTokenValid(expiredToken);

		StepVerifier.create(result).expectNext(false).verifyComplete();
	}
}
