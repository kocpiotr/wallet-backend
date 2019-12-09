package com.example.demo.user;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	public Set<User> findAll();
}
