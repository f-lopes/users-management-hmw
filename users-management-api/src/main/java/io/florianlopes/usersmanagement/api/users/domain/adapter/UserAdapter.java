package io.florianlopes.usersmanagement.api.users.domain.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.florianlopes.usersmanagement.api.users.domain.model.User;

public interface UserAdapter {

    User createUser(User user);

    Page<User> getAllUsers(Pageable pageable);

    Optional<User> getUserById(String id);
}
