package io.florianlopes.usersmanagement.api.users.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.ListUsersQuery;

@ExtendWith(MockitoExtension.class)
class ListUsersServiceTests {

    @Mock private UserAdapter userAdapter;

    private ListUsersService listUsersService;

    @BeforeEach
    void setUp() {
        this.listUsersService = new ListUsersService(this.userAdapter);
    }

    @Test
    void listUsersShouldReturnPageOfUsers() {
        final ListUsersQuery listUsersQuery = new ListUsersQuery(PageRequest.of(0, 10));
        final ArrayList<User> usersFromAdapter = new ArrayList<>();
        final PageImpl<User> pagesOfUsers = new PageImpl<>(usersFromAdapter);
        when(this.userAdapter.getAllUsers(listUsersQuery.getPageable())).thenReturn(pagesOfUsers);

        final Page<User> users = this.listUsersService.listUsers(listUsersQuery);
        assertThat(users).isNotNull();
        assertThat(users).isEqualTo(pagesOfUsers);

        verify(this.userAdapter).getAllUsers(listUsersQuery.getPageable());
    }
}
