package cat.itacademy.s05.t02.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.repositories.UserRepository;
import cat.itacademy.s05.t02.models.*;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			var userObj = user.get();
			return UserDetails.builder().username(userObj.getUsername()).password(userObj.getPassword())
					.roles(getRoles(userObj)).build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	private String[] getRoles(User user) {
		if (user.getRoles() == null) {
			return new String [] {"user"};
		}
		return user.getRoles().split{","};
	}

}
