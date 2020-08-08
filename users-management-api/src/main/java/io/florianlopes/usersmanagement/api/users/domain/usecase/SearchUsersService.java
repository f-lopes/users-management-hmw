package io.florianlopes.usersmanagement.api.users.domain.usecase;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.SearchUsersQuery;

@Service
@Transactional(readOnly = true)
public class SearchUsersService implements SearchUsersUseCase {

    private final UserAdapter userAdapter;

    public SearchUsersService(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public Page<User> searchUsers(SearchUsersQuery searchUsersQuery) {
        return this.userAdapter.getUsers(
                searchUsersQuery.getPageable(), searchUsersQuery.getUserFilter());
    }
}
