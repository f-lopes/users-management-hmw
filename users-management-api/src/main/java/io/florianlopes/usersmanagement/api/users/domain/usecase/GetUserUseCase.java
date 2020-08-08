package io.florianlopes.usersmanagement.api.users.domain.usecase;

import io.florianlopes.usersmanagement.api.users.domain.model.User;

public interface GetUserUseCase {

    User getUserById(String id);
}
