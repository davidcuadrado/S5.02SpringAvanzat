package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
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
    }

    @Test
    void filter_withValidToken_authenticatesAndProceeds() {
        // Configuración del token y usuario válido
        String token = "validToken";
        when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.validateAndExtractUsername(token)).thenReturn(Mono.just("username"));
        when(userDetailService.findByUsername("username")).thenReturn(Mono.just(userDetails));
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Ejecución del método
        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        // Verificación de la ejecución exitosa
        StepVerifier.create(result)
            .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void filter_withInvalidToken_returnsUnauthorized() {
        // Configuración del token inválido
        String token = "invalidToken";
        when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.validateAndExtractUsername(token)).thenReturn(Mono.empty());

        // Simulación de la respuesta con UNAUTHORIZED
        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenReturn(null);
        when(response.setComplete()).thenReturn(Mono.empty());

        // Ejecución del método
        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        // Verificación de la respuesta UNAUTHORIZED
        StepVerifier.create(result)
            .verifyComplete();

        verify(response, times(1)).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response, times(1)).setComplete();
        verify(chain, times(0)).filter(exchange);
    }

    @Test
    void extractToken_withValidAuthorizationHeader_returnsToken() {
        // Configuración del header válido
        String token = "validToken";
        String header = "Bearer " + token;

        // Ejecución del método
        String extractedToken = jwtAuthenticationFilter.extractToken(header);

        // Verificación del token extraído
        assertEquals(token, extractedToken);
    }

    @Test
    void extractToken_withInvalidAuthorizationHeader_returnsNull() {
        // Configuración de un header inválido
        String header = "InvalidHeader";

        // Ejecución del método
        String extractedToken = jwtAuthenticationFilter.extractToken(header);

        // Verificación del token extraído (debe ser null)
        assertNull(extractedToken);
    }
}
