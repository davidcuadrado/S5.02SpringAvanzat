package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
/*
class AuthenticationSuccessHandlerTest {

	@InjectMocks
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Mock
	private WebFilterExchange webFilterExchange;

	@Mock
	private Authentication authentication;

	@Mock
	private ServerWebExchange serverWebExchange;

	@Mock
	private ServerHttpResponse response;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(webFilterExchange.getExchange()).thenReturn(serverWebExchange);
		when(serverWebExchange.getResponse()).thenReturn(response);
	}

	@Test
	void onAuthenticationSuccess_withAdminRole_redirectsToAdminHome() {
	    GrantedAuthority adminAuthority = mock(GrantedAuthority.class);
	    when(adminAuthority.getAuthority()).thenReturn("ROLE_ADMIN");

	    when(authentication.getAuthorities()).thenAnswer(invocation -> Collections.singletonList(adminAuthority));

	    HttpHeaders headers = new HttpHeaders();
	    when(response.getHeaders()).thenReturn(headers);
	    when(response.setComplete()).thenReturn(Mono.empty());

	    Mono<Void> result = authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);

	    StepVerifier.create(result).verifyComplete();

	    verify(response).setStatusCode(HttpStatus.FOUND);
	    assertEquals(URI.create("/admin/home"), headers.getLocation());
	    verify(response).setComplete();
	}


	@Test
	void onAuthenticationSuccess_withUserRole_redirectsToUserHome() {
		GrantedAuthority userAuthority = mock(GrantedAuthority.class);
		when(userAuthority.getAuthority()).thenReturn("ROLE_USER");

		when(authentication.getAuthorities()).thenAnswer(invocation -> Collections.singletonList(userAuthority));

		HttpHeaders headers = new HttpHeaders();
		when(response.getHeaders()).thenReturn(headers);
		when(response.setComplete()).thenReturn(Mono.empty());

		Mono<Void> result = authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);

		StepVerifier.create(result).verifyComplete();

		verify(response).setStatusCode(HttpStatus.FOUND);
		assertEquals(URI.create("/user/home"), headers.getLocation()); 
		verify(response).setComplete();
	}

	@Test
	void setRedirectResponse_setsStatusAndLocation() {
		String redirectUrl = "/user/home";
		HttpHeaders headers = new HttpHeaders();

		when(response.getHeaders()).thenReturn(headers);

		authenticationSuccessHandler.setRedirectResponse(serverWebExchange, redirectUrl);

		verify(response).setStatusCode(HttpStatus.FOUND);
		assertEquals(URI.create(redirectUrl), headers.getLocation());
	}

}
*/
