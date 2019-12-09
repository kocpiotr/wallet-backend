package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@SpringBootTest
class WaletBackendApplicationTests {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void contextLoads() {
		
		User user = new User("Malgosia");
		
		userRepository.save(user);
		
		System.out.println(user.getId());
		
	}

}
