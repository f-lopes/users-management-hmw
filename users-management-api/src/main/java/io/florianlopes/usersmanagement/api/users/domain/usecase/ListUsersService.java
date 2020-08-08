package io.florianlopes.usersmanagement.api.users.domain.usecase;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.ListUsersQuery;

@Service
@Transactional(readOnly = true)
class ListUsersService implements ListUsersUseCase {

    private final UserAdapter userAdapter;

    public ListUsersService(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public Page<User> listUsers(ListUsersQuery listUsersQuery) {
        return this.userAdapter.getAllUsers(listUsersQuery.getPageable());
    }
}
