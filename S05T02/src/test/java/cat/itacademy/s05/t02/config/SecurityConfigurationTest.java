package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import cat.itacademy.s05.t02.services.MyUserDetailService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class SecurityConfigurationTest {

    @InjectMocks
    private SecurityConfiguration securityConfiguration;

    @Mock
    private MyUserDetailService userDetailService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private ServerHttpSecurity http;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void securityWebFilterChain_configuresSecurityFilters() {
    	
        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = mock(ServerHttpSecurity.AuthorizeExchangeSpec.class);
        ServerHttpSecurity.AuthorizeExchangeSpec.PathMatchersExchangeSpec pathMatchersExchangeSpec = mock(ServerHttpSecurity.AuthorizeExchangeSpec.PathMatchersExchangeSpec.class);

        when(http.csrf()).thenReturn(mock(ServerHttpSecurity.CsrfSpec.class));
        when(http.authorizeExchange()).thenReturn(authorizeExchangeSpec);
        when(authorizeExchangeSpec.matchers(any())).thenReturn(pathMatchersExchangeSpec);
        when(pathMatchersExchangeSpec.permitAll()).thenReturn(pathMatchersExchangeSpec);
        when(pathMatchersExchangeSpec.pathMatchers("/user/**", "/pet/**")).thenReturn(pathMatchersExchangeSpec);
        when(pathMatchersExchangeSpec.pathMatchers("/admin/**", "/user/**", "/pet/**")).thenReturn(pathMatchersExchangeSpec);
        when(http.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.build()).thenReturn(mock(SecurityWebFilterChain.class));
        
        SecurityWebFilterChain filterChain = securityConfiguration.securityWebFilterChain(http);

        verify(http, times(1)).addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        verify(http, times(1)).formLogin(any());
    }

    @Test
    void userDetailsService_returnsUserDetailServiceBean() {
        ReactiveUserDetailsService userDetailsService = securityConfiguration.userDetailsService();
        assertEquals(userDetailService, userDetailsService);
    }

    @Test
    void authenticationManager_returnsAuthenticationManager() {
        ReactiveAuthenticationManager authManager = securityConfiguration.authenticationManager();

        assertTrue(authManager instanceof UserDetailsRepositoryReactiveAuthenticationManager);
    }

    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();

        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
}
