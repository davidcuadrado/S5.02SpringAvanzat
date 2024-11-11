package cat.itacademy.s05.t02.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

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
        when(authentication.getAuthorities()).thenReturn(Collections.singletonList(adminAuthority));
        
        when(response.setStatusCode(HttpStatus.FOUND)).thenReturn(null);
        when(response.getHeaders().setLocation(any(URI.class))).thenReturn(null);
        when(response.setComplete()).thenReturn(Mono.empty());

        Mono<Void> result = authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);

        StepVerifier.create(result)
            .verifyComplete();

        verify(response, times(1)).setStatusCode(HttpStatus.FOUND);
        verify(response.getHeaders(), times(1)).setLocation(URI.create("/admin/home"));
        verify(response, times(1)).setComplete();
    }

    @Test
    void onAuthenticationSuccess_withUserRole_redirectsToUserHome() {
        // Configuración del rol USER
        GrantedAuthority userAuthority = mock(GrantedAuthority.class);
        when(userAuthority.getAuthority()).thenReturn("ROLE_USER");
        when(authentication.getAuthorities()).thenReturn(Collections.singletonList(userAuthority));

        // Simulación de respuesta
        when(response.setStatusCode(HttpStatus.FOUND)).thenReturn(null);
        when(response.getHeaders().setLocation(any(URI.class))).thenReturn(null);
        when(response.setComplete()).thenReturn(Mono.empty());

        // Ejecución del método
        Mono<Void> result = authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);

        // Verificación del resultado
        StepVerifier.create(result)
            .verifyComplete();

        verify(response, times(1)).setStatusCode(HttpStatus.FOUND);
        verify(response.getHeaders(), times(1)).setLocation(URI.create("/user/home"));
        verify(response, times(1)).setComplete();
    }
}
