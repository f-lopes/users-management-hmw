package io.florianlopes.usersmanagement.api.users.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import io.florianlopes.usersmanagement.api.users.domain.command.CreateUserCommand;
import io.florianlopes.usersmanagement.api.users.domain.exception.UserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.usecase.CreateUserUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.CreateUserRequest;

@RestController
@RequestMapping("/v1/users")
public class CreateUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserController.class);

    private final CreateUserUseCase createUserUseCase;

    public CreateUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Validated CreateUserRequest createUserRequest,
            HttpServletRequest httpServletRequest)
            throws UserCreationException {
        final String clientIpAddress = httpServletRequest.getRemoteAddr();
        LOGGER.info(
                "Received request to create user {} from IP {}",
                createUserRequest,
                clientIpAddress);
        final User createdUser =
                this.createUserUseCase.createUser(
                        toCreateUserCommand(createUserRequest, clientIpAddress));
        LOGGER.info("Created user {} from IP {}", createdUser, clientIpAddress);
        return ResponseEntity.created(
                        MvcUriComponentsBuilder.fromController(this.getClass())
                                .path("/{id}")
                                .buildAndExpand(createdUser.getId())
                                .toUri())
                .build();
    }

    private CreateUserCommand toCreateUserCommand(
            CreateUserRequest createUserRequest, String clientIpAddress) {
        return new CreateUserCommand(
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                clientIpAddress);
    }
}
