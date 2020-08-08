package io.florianlopes.usersmanagement.api.users.domain.ipapi.exception;

public class InvalidIpAddressIpApiException extends IpApiException {

    public InvalidIpAddressIpApiException(String message) {
        super(message);
    }

    public InvalidIpAddressIpApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIpAddressIpApiException(Throwable cause) {
        super(cause);
    }
}
