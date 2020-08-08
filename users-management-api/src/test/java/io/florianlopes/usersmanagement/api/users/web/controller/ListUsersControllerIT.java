package io.florianlopes.usersmanagement.api.users.web.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import io.florianlopes.usersmanagement.api.common.web.PageDtoMapperImpl;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.usecase.ListUsersUseCase;
import io.florianlopes.usersmanagement.api.users.web.mapper.WebUserMapper;

@WebMvcTest(controllers = ListUsersController.class)
@Import({WebUserMapper.class, PageDtoMapperImpl.class})
@TestPropertySource(properties = {"embedded.containers.enabled=false"})
class ListUsersControllerIT {

    @Autowired private MockMvc mockMvc;

    @MockBean private ListUsersUseCase listUsersUseCase;

    @Test
    @DisplayName("GET - /v1/users")
    void shouldReturnAllUsers() throws Exception {
        final ArrayList<User> users = new ArrayList<>();
        users.add(new User("1234", "John", "Doe", "john.doe@email.com", "test"));
        when(this.listUsersUseCase.listUsers(any()))
                .thenReturn(new PageImpl<>(users, PageRequest.of(0, 10), 10));

        this.mockMvc
                .perform(
                        get("/v1/users")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));
    }
}
