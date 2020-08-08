package io.florianlopes.usersmanagement.api.users.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import io.florianlopes.usersmanagement.api.users.persistence.entity.UserDocument;

public interface UserRepository
        extends MongoRepository<UserDocument, String>, QuerydslPredicateExecutor<UserDocument> {}
