package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.repositories.MyUserRepository;
import cat.itacademy.s05.t02.models.MyUser;
import reactor.core.publisher.Mono;

@Service
public class MyUserDetailService implements ReactiveUserDetailsService {

	@Autowired
	private MyUserRepository userRepository;

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByUsername(username)
			.map(user -> User.builder()
					.username(user.getUsername())
					.password(user.getPassword())
					.roles(getRoles(user))
					.build())
			.switchIfEmpty(Mono.error(new UsernameNotFoundException(username)));
	}

	private String[] getRoles(MyUser user) {
		if (user.getRole() == null) {
			return new String[] {"USER"};
		}
		return user.getRole().split(",");
	}
}
