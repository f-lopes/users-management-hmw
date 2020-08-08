package io.florianlopes.usersmanagement.api.users.domain.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.UserFilter;

public interface UserAdapter {

    User createUser(User user);

    Page<User> getAllUsers(Pageable pageable);

    Optional<User> getUserById(String id);

    Page<User> getUsers(Pageable pageable, UserFilter userFilter);
}
