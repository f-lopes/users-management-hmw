package io.florianlopes.usersmanagement.api.users.web.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import io.florianlopes.usersmanagement.api.users.domain.exception.ResourceNotFoundException;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.usecase.GetUserUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.UserDto;
import io.florianlopes.usersmanagement.api.users.web.mapper.UserMapper;

@WebMvcTest(controllers = GetUserController.class)
@TestPropertySource(properties = {"embedded.containers.enabled=false"})
class GetUserControllerIT {

    @Autowired private MockMvc mockMvc;

    @MockBean private GetUserUseCase getUserUseCase;

    @MockBean private UserMapper userMapper;

    @Test
    @DisplayName("GET - /v1/users/{id}")
    void shouldReturnUserById() throws Exception {
        final User user = new User("1", "John", "Doe", "john.doe@email.com", "test");
        when(this.getUserUseCase.getUserById("1")).thenReturn(user);
        when(this.userMapper.asUserDto(user)).thenReturn(toUserDto(user));

        this.mockMvc
                .perform(
                        get("/v1/users/1")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(user.getId())))
                .andExpect(jsonPath("$.firstName", equalTo(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(user.getLastName())))
                .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
                .andExpect(jsonPath("$.password", equalTo(user.getPassword())));
    }

    @Test
    @DisplayName("GET - /v1/users/{id} - 404")
    void userNotFoundShouldReturn404() throws Exception {
        when(this.getUserUseCase.getUserById("1"))
                .thenThrow(new ResourceNotFoundException(User.class, "1"));

        this.mockMvc
                .perform(
                        get("/v1/users/1")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.path", equalTo("/v1/users/1")))
                .andExpect(jsonPath("$.message", equalTo("Resource User with id 1 not found")));
    }

    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword());
    }
}
