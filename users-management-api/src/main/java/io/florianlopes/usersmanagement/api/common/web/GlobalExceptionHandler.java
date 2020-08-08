package io.florianlopes.usersmanagement.api.common.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import io.florianlopes.usersmanagement.api.common.web.dto.ErrorDto;
import io.florianlopes.usersmanagement.api.common.web.dto.ValidationErrorDto;
import io.florianlopes.usersmanagement.api.users.domain.exception.UnauthorizedUserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.exception.UserCreationException;

@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
        LOGGER.info("Resolving exception", exception);
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus)
                .body(
                        new ErrorDto(
                                httpStatus.value(),
                                getRequestPath(request),
                                exception.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(
            DataAccessException dataAccessException, HttpServletRequest request) {
        LOGGER.info("Resolving exception", dataAccessException);
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus)
                .body(
                        new ErrorDto(
                                httpStatus.value(),
                                getRequestPath(request),
                                dataAccessException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest request) {
        LOGGER.info("Resolving exception", methodArgumentNotValidException);
        final ValidationErrorDto errorDto =
                new ValidationErrorDto(
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request),
                        "Validation error");
        errorDto.getValidationErrors()
                .addAll(getValidationErrors(methodArgumentNotValidException.getBindingResult()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(
            UserCreationException userCreationException, HttpServletRequest request) {
        LOGGER.info("Resolving exception", userCreationException);
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus)
                .body(
                        new ErrorDto(
                                httpStatus.value(),
                                getRequestPath(request),
                                userCreationException.getMessage()));
    }

    @ExceptionHandler(UnauthorizedUserCreationException.class)
    public ResponseEntity<?> handleUnauthorizedUserCreationException(
            UnauthorizedUserCreationException unauthorizedUserCreationException,
            HttpServletRequest request) {
        LOGGER.info("Resolving exception", unauthorizedUserCreationException);
        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
                .body(
                        new ErrorDto(
                                httpStatus.value(),
                                getRequestPath(request),
                                unauthorizedUserCreationException.getMessage()));
    }

    private List<ValidationErrorDto.ValidationError> getValidationErrors(
            BindingResult bindingResult) {
        final List<ValidationErrorDto.ValidationError> validationErrors = new ArrayList<>();

        if (bindingResult != null) {
            bindingResult
                    .getFieldErrors()
                    .forEach(
                            fieldError ->
                                    validationErrors.add(
                                            new ValidationErrorDto.ValidationError(
                                                    fieldError.getField(),
                                                    orEmpty(fieldError),
                                                    fieldError.getDefaultMessage())));
            bindingResult
                    .getGlobalErrors()
                    .forEach(
                            globalError ->
                                    validationErrors.add(
                                            new ValidationErrorDto.ValidationError(
                                                    globalError.getObjectName(),
                                                    globalError.getObjectName(),
                                                    globalError.getDefaultMessage())));
        }

        return validationErrors;
    }

    private Object orEmpty(FieldError fieldError) {
        return fieldError.getRejectedValue() == null
                ? StringUtils.EMPTY
                : fieldError.getRejectedValue();
    }

    private String getRequestPath(HttpServletRequest request) {
        return UriComponentsBuilder.fromPath(
                        StringUtils.isEmpty(request.getServletPath())
                                ? request.getPathInfo()
                                : request.getServletPath())
                .query(request.getQueryString())
                .build()
                .toUriString();
    }
}
