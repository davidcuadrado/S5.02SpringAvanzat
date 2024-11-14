package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import cat.itacademy.s05.t02.services.JwtService;
import cat.itacademy.s05.t02.services.MyUserDetailService;

class JwtAuthenticationFilterTest {

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Mock
	private JwtService jwtService;

	@Mock
	private MyUserDetailService userDetailService;

	@Mock
	private ServerWebExchange exchange;

	@Mock
	private WebFilterChain chain;

	@Mock
	private ServerHttpRequest request;

	@Mock
	private ServerHttpResponse response;

	@Mock
	private HttpHeaders headers;

	@Mock
	private UserDetails userDetails;

	@BeforeEach
	void setUp() {
	    MockitoAnnotations.openMocks(this);

	    when(exchange.getRequest()).thenReturn(request);
	    when(exchange.getResponse()).thenReturn(response);
	    when(request.getHeaders()).thenReturn(headers);

	    RequestPath requestPath = Mockito.mock(RequestPath.class);
	    when(request.getPath()).thenReturn(requestPath);
	    when(requestPath.value()).thenReturn("/somePath");
	}

	@Test
	void filter_withValidToken_authenticatesAndProceeds() {
		String token = "validToken";
		when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
		when(jwtService.validateAndExtractUsername(token)).thenReturn(Mono.just("username"));
		when(userDetailService.findByUsername("username")).thenReturn(Mono.just(userDetails));
		when(userDetails.getAuthorities()).thenReturn(new ArrayList<>()); // Mock empty authorities
		when(chain.filter(exchange)).thenReturn(Mono.empty());

		Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

		StepVerifier.create(result).verifyComplete();
	}

	@Test
	void filter_withInvalidToken_returnsUnauthorized() {
		String token = "invalidToken";
		when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
		when(jwtService.validateAndExtractUsername(token))
				.thenReturn(Mono.error(new IllegalArgumentException("Invalid token")));

		doReturn(true).when(response).setStatusCode(HttpStatus.UNAUTHORIZED);
		when(response.setComplete()).thenReturn(Mono.empty());

		Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

		StepVerifier.create(result).verifyComplete();
	}

	@Test
	void extractToken_withValidAuthorizationHeader_returnsToken() {
		String token = "validToken";
		String header = "Bearer " + token;

		String extractedToken = jwtAuthenticationFilter.extractToken(header);

		assertEquals(token, extractedToken);
	}

	@Test
	void extractToken_withInvalidAuthorizationHeader_returnsNull() {
		String header = "InvalidHeader";

		String extractedToken = jwtAuthenticationFilter.extractToken(header);

		assertNull(extractedToken);
	}
}
