package com.example.demo.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>>  {

	@Override
	public EntityModel<User> toModel(User entity) {
		return new EntityModel<>(entity, 
				linkTo(methodOn(UserController.class).create(entity)).withSelfRel(),
				linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
	}
	
	@Override
	public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
		CollectionModel<EntityModel<User>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
		collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
		return collectionModel;
	}

}
