package io.florianlopes.usersmanagement.api.users.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.florianlopes.usersmanagement.api.users.domain.usecase.GetUserUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.UserDto;
import io.florianlopes.usersmanagement.api.users.web.mapper.UserMapper;

@RestController
@RequestMapping("/v1/users")
public class GetUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserController.class);

    private final GetUserUseCase getUserUseCase;
    private final UserMapper userMapper;

    public GetUserController(GetUserUseCase getUserUseCase, UserMapper userMapper) {
        this.getUserUseCase = getUserUseCase;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) {
        LOGGER.info("Received request to get user with id {}", id);
        return this.userMapper.asUserDto(this.getUserUseCase.getUserById(id));
    }
}
