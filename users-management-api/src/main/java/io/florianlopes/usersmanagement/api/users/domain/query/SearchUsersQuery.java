package io.florianlopes.usersmanagement.api.users.domain.query;

import org.springframework.data.domain.Pageable;

public class SearchUsersQuery {

    private final Pageable pageable;
    private final UserFilter userFilter;

    public SearchUsersQuery(Pageable pageable, UserFilter userFilter) {
        this.pageable = pageable;
        this.userFilter = userFilter;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public UserFilter getUserFilter() {
        return userFilter;
    }

    @Override
    public String toString() {
        return "SearchUsersQuery{" + "pageable=" + pageable + ", userFilter=" + userFilter + '}';
    }
}
