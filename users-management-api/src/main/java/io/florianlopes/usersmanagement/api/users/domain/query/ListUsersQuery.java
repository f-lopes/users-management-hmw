package io.florianlopes.usersmanagement.api.users.domain.query;

import org.springframework.data.domain.Pageable;

public class ListUsersQuery {

    private final Pageable pageable;

    public ListUsersQuery(Pageable pageable) {
        this.pageable = pageable;
    }

    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public String toString() {
        return "ListUsersQuery{" + "pageable=" + pageable + '}';
    }
}
