package io.florianlopes.usersmanagement.api.users.domain.listener.event;

public abstract class UserEvent {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;

    public UserEvent() {}

    public UserEvent(
            String userFirstName, String userLastName, String userEmail, String userPassword) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return "UserEvent{"
                + "userFirstName='"
                + userFirstName
                + '\''
                + ", userLastName='"
                + userLastName
                + '\''
                + ", userEmail='"
                + userEmail
                + '\''
                + '}';
    }
}
