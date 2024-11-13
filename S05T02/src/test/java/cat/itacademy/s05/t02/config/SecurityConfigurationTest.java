package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import cat.itacademy.s05.t02.services.MyUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SecurityConfigurationTest {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ServerHttpSecurity http;
    
    @MockBean
    private MyUserDetailService userDetailService;

    /*
    @Test
    void contextLoads() {
        assertNotNull(securityConfiguration);
        assertNotNull(http);
    }

    @Test
    void securityWebFilterChain_configuresSecurityFilters() {
        SecurityWebFilterChain filterChain = securityConfiguration.securityWebFilterChain(http);

        assertNotNull(filterChain);
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
    */
}
