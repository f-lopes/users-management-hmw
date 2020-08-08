package io.florianlopes.usersmanagement.api.users.domain.command;

public class CreateUserCommand {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String ipAddress;

    public CreateUserCommand(
            String firstName, String lastName, String email, String password, String ipAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.ipAddress = ipAddress;
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

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "CreateUserCommand{"
                + "firstName='"
                + firstName
                + '\''
                + ", lastName='"
                + lastName
                + '\''
                + ", email='"
                + email
                + '\''
                + ", ipAddress='"
                + ipAddress
                + '\''
                + '}';
    }
}
