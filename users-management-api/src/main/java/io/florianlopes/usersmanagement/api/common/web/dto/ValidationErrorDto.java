package io.florianlopes.usersmanagement.api.common.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto extends ErrorDto {
    public ValidationErrorDto(int status, String path, String message) {
        super(status, path, message);
    }

    private List<ValidationError> validationErrors;

    public List<ValidationError> getValidationErrors() {
        if (this.validationErrors == null) {
            this.validationErrors = new ArrayList<>();
        }
        return validationErrors;
    }

    public static class ValidationError implements Serializable {
        private final String field;
        private final Object rejected;
        private final String message;

        public ValidationError(String field, Object rejected, String message) {
            this.field = field;
            this.rejected = rejected;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public Object getRejected() {
            return rejected;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Error{"
                    + "field='"
                    + field
                    + '\''
                    + ", rejected="
                    + rejected
                    + ", message='"
                    + message
                    + '\''
                    + '}';
        }
    }
}
