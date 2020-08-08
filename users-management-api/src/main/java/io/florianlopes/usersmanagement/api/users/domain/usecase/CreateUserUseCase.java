package io.florianlopes.usersmanagement.api.users.domain.usecase;

import io.florianlopes.usersmanagement.api.users.domain.command.CreateUserCommand;
import io.florianlopes.usersmanagement.api.users.domain.exception.UserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.model.User;

public interface CreateUserUseCase {

    User createUser(CreateUserCommand createUserCommand) throws UserCreationException;
}
