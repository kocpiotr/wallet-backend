package com.example.demo.user;

import java.util.Optional;
import java.util.Set;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	UserRepository userRepository;
	UserModelAssembler assembler;
	
	public UserController(UserRepository userRepository, UserModelAssembler assembler) {
		this.userRepository = userRepository;
		this.assembler = assembler;
	}

	@PostMapping("/users")
	public ResponseEntity<EntityModel<User>> create(@RequestBody User user) {
		if (user.getId() != null && userRepository.findById(user.getId()) != null) {
			throw new IllegalArgumentException("This endpoint does not support update operation");
		}
		
		final User createdUser = userRepository.save(user);
		final EntityModel<User> model = assembler.toModel(createdUser);
		
		return new ResponseEntity<EntityModel<User>>(model, HttpStatus.CREATED);
	}
	
	@GetMapping("/users")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
		final Set<User> items = userRepository.findAll();
		final CollectionModel<EntityModel<User>> model = assembler.toCollectionModel(items);
		return new ResponseEntity<CollectionModel<EntityModel<User>>>(model, HttpStatus.OK);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long id) {
		final Optional<User> user = userRepository.findById(id);
		final EntityModel<User> model = assembler.toModel(user.get());
		
		return new ResponseEntity<EntityModel<User>>(model, HttpStatus.OK);
	}
	
	@PatchMapping("/users")
	public ResponseEntity<EntityModel<User>> update(@RequestBody User user) {
		if (user.getId() == null) {
			throw new IllegalArgumentException("This endpoint does not support create operation");
		}
		
		final User savedUser = userRepository.save(user);
		final EntityModel<User> model = assembler.toModel(savedUser);
		
		return new ResponseEntity<EntityModel<User>>(model, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		userRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
