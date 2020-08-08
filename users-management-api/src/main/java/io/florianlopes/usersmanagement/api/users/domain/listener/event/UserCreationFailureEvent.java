package io.florianlopes.usersmanagement.api.users.domain.listener.event;

public class UserCreationFailureEvent extends UserEvent {

    public UserCreationFailureEvent(
            String userFirstName, String userLastName, String userEmail, String userPassword) {
        super(userFirstName, userLastName, userEmail, userPassword);
    }
}
