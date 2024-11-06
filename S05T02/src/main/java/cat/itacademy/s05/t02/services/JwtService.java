package cat.itacademy.s05.t02.services;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Service
public class JwtService {

	private static final String SECRET = "E0D1A0FDE7DECE0CF1FB3212E468DCCAA9B8707334E6CD50F0DEB47FE679FFF3";
	private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

	public Mono<String> generateToken(Mono<UserDetails> userDetailsMono) {
		return userDetailsMono.flatMap(userDetails -> {
			return Mono.fromCallable(() -> {
				Map<String, Object> claims = new HashMap<>();
				claims.put("iss", "ITAcademyS05T02");

				return Jwts.builder().claims(claims).subject(userDetails.getUsername())
						.issuedAt(Date.from(Instant.now())).expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
						.signWith(generateKey()).compact();
			});
		});
	}

	private SecretKey generateKey() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	public Mono<String> extractUsername(String jwt) {
		return Mono.fromCallable(() -> getClaims(jwt).getSubject());
	}
	
	public Mono<String> extractUserId(String jwt) {
        return Mono.fromCallable(() -> getClaims(jwt).get("userId", String.class));
    }

	private Claims getClaims(String jwt) {
		return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(jwt).getPayload();
	}

	public Mono<Boolean> isTokenValid(String jwt) {
		return Mono.fromCallable(() -> {
			try {
				Claims claims = getClaims(jwt);
				Date expiration = claims.getExpiration();
				return expiration != null && expiration.after(Date.from(Instant.now()));
			} catch (MalformedJwtException | IllegalArgumentException e) {
				return false;
			}
		});
	}

}
