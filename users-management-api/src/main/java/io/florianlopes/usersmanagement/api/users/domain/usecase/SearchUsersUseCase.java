package io.florianlopes.usersmanagement.api.users.domain.usecase;

import org.springframework.data.domain.Page;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.SearchUsersQuery;

public interface SearchUsersUseCase {

    Page<User> searchUsers(SearchUsersQuery searchUsersQuery);
}
