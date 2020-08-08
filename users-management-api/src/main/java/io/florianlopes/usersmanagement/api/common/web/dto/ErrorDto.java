package io.florianlopes.usersmanagement.api.common.web.dto;

public class ErrorDto {

    private final int status;
    private final String path;
    private final String message;

    public ErrorDto(int status, String path, String message) {
        this.status = status;
        this.path = path;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
