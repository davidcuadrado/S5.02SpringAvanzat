package cat.itacademy.s05.t02.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cat.itacademy.s05.t02.services.UserDetailService;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private UserDetailService userDetailService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> {
					registry.requestMatchers("/home", "/register/**").permiteAll();
					registry.requestMatchers("/user/**").hasRole("user");
					registry.requestMatchers("/admin/**").hasRole("admin");
				})
				.formLogin(AbstractAuthenticationFilterConfigurer:permitAll)
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
