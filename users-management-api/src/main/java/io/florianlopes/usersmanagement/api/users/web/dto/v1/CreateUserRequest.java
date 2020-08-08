package io.florianlopes.usersmanagement.api.users.web.dto.v1;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CreateUserRequest {

    @NotEmpty private final String firstName;

    @NotEmpty private final String lastName;
    @NotEmpty @Email private final String email;

    @NotEmpty private final String password;

    public CreateUserRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{"
                + "firstName='"
                + firstName
                + '\''
                + ", lastName='"
                + lastName
                + '\''
                + ", email='"
                + email
                + '\''
                + '}';
    }
}
