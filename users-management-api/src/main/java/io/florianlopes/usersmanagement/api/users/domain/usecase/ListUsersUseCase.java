package io.florianlopes.usersmanagement.api.users.domain.usecase;

import org.springframework.data.domain.Page;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.ListUsersQuery;

public interface ListUsersUseCase {

    Page<User> listUsers(ListUsersQuery listUsersQuery);
}
