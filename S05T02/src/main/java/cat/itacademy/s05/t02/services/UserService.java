package cat.itacademy.s05.t02.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t02.repositories.MyUserRepository;

@Service
public class UserService {
	
	@Autowired
	private MyUserRepository userRepository;
	
	

}
