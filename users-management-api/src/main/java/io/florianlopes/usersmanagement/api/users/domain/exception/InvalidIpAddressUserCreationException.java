package io.florianlopes.usersmanagement.api.users.domain.exception;

public class InvalidIpAddressUserCreationException extends UserCreationException {

    public InvalidIpAddressUserCreationException(String message) {
        super(message);
    }

    public InvalidIpAddressUserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIpAddressUserCreationException(Throwable cause) {
        super(cause);
    }
}
