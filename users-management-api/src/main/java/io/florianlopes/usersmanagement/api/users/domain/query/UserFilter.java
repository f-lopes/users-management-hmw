package io.florianlopes.usersmanagement.api.users.domain.query;

public class UserFilter {

    private final String firstName;
    private final String lastName;
    private final String email;

    public UserFilter(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
