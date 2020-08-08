package io.florianlopes.usersmanagement.api.users.domain.exception;

public class UnauthorizedUserCreationException extends UserCreationException {

    public UnauthorizedUserCreationException(String message) {
        super(message);
    }

    public UnauthorizedUserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedUserCreationException(Throwable cause) {
        super(cause);
    }
}
