package io.florianlopes.usersmanagement.api.users.web.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.florianlopes.usersmanagement.api.users.domain.command.CreateUserCommand;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.usecase.CreateUserUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.CreateUserRequest;

@WebMvcTest(controllers = CreateUserController.class)
@TestPropertySource(properties = {"embedded.containers.enabled=false"})
class CreateUserControllerIT {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private MockMvc mockMvc;

    @MockBean private CreateUserUseCase createUserService;

    @Test
    @DisplayName("POST - /v1/users - Invalid input")
    void invalidInputShouldReturnValidationErrors() throws Exception {
        final CreateUserRequest createUserRequest =
                new CreateUserRequest("", "Doe", "john.doe@email.com", "test");

        this.mockMvc
                .perform(
                        post("/v1/users")
                                .content(toJson(createUserRequest))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.path", equalTo("/v1/users")))
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)));
    }

    @Test
    @DisplayName("POST - /v1/users - valid input")
    void validInputShouldCreateUser() throws Exception {
        final CreateUserRequest createUserRequest =
                new CreateUserRequest("John", "Doe", "john.doe@email.com", "test");
        when(this.createUserService.createUser(any(CreateUserCommand.class)))
                .thenReturn(toUser(createUserRequest));

        this.mockMvc
                .perform(
                        post("/v1/users")
                                .content(toJson(createUserRequest))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header("REMOTE_ADDR", "1234"))
                .andExpect(status().isCreated());

        verify(this.createUserService).createUser(any(CreateUserCommand.class));
    }

    private User toUser(CreateUserRequest createUserRequest) {
        return new User(
                RandomStringUtils.random(5),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword());
    }

    private CreateUserCommand toCreateUserCommand(CreateUserRequest createUserRequest) {
        return new CreateUserCommand(
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                null);
    }

    private String toJson(CreateUserRequest createUserRequest) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(createUserRequest);
    }
}
