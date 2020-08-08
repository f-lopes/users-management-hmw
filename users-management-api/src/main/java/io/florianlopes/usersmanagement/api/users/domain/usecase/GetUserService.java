package io.florianlopes.usersmanagement.api.users.domain.usecase;

import org.springframework.stereotype.Service;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.exception.ResourceNotFoundException;
import io.florianlopes.usersmanagement.api.users.domain.model.User;

@Service
public class GetUserService implements GetUserUseCase {

    private final UserAdapter userAdapter;

    public GetUserService(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public User getUserById(String id) {
        return this.userAdapter
                .getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }
}
