package io.florianlopes.usersmanagement.api.users.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.florianlopes.usersmanagement.api.common.web.PageDtoMapper;
import io.florianlopes.usersmanagement.api.common.web.dto.PageDto;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.ListUsersQuery;
import io.florianlopes.usersmanagement.api.users.domain.usecase.ListUsersUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.UserDto;
import io.florianlopes.usersmanagement.api.users.web.mapper.UserMapper;

@RestController
@RequestMapping("/v1/users")
public class ListUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListUsersController.class);

    private final ListUsersUseCase listUsersUseCase;
    private final UserMapper userMapper;
    private final PageDtoMapper pageDtoMapper;

    public ListUsersController(
            ListUsersUseCase listUsersUseCase, UserMapper userMapper, PageDtoMapper pageDtoMapper) {
        this.listUsersUseCase = listUsersUseCase;
        this.userMapper = userMapper;
        this.pageDtoMapper = pageDtoMapper;
    }

    @GetMapping
    public PageDto<UserDto> getAllUsers(Pageable pageable) {
        LOGGER.info("Received request to list all users with pageable {}", pageable);
        final Page<User> users = this.listUsersUseCase.listUsers(new ListUsersQuery(pageable));
        final Page<UserDto> usersDto = users.map(this.userMapper::asUserDto);
        LOGGER.info("Found {} users}", users.getTotalElements());
        return this.pageDtoMapper.asPageDto(usersDto);
    }
}
