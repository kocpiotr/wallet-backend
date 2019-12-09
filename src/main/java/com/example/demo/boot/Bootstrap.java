package com.example.demo.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Bootstrap{

	@Autowired
	UserRepository userRepository;
	
	public Bootstrap(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			log.info("Preloading " + userRepository.save(new User("Malgosia")));
			log.info("Preloading " + userRepository.save(new User("Piotr")));
		};
	}
}
