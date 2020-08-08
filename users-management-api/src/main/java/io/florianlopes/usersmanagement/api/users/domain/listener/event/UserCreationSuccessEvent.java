package io.florianlopes.usersmanagement.api.users.domain.listener.event;

public class UserCreationSuccessEvent extends UserEvent {

    private String userId;

    public UserCreationSuccessEvent() {}

    public UserCreationSuccessEvent(
            String userFirstName, String userLastName, String userEmail, String userPassword) {
        super(userFirstName, userLastName, userEmail, userPassword);
    }

    public UserCreationSuccessEvent(
            String userId,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPassword) {
        super(userFirstName, userLastName, userEmail, userPassword);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserCreationSuccessEvent{" + "userId='" + userId + '\'' + '}';
    }
}
