package io.florianlopes.usersmanagement.api.users.domain.ipapi.exception;

public class IpApiException extends Exception {

    public IpApiException(String message) {
        super(message);
    }

    public IpApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IpApiException(Throwable cause) {
        super(cause);
    }
}
