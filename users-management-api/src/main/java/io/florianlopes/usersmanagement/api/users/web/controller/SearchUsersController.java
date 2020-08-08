package io.florianlopes.usersmanagement.api.users.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.florianlopes.usersmanagement.api.common.web.PageDtoMapper;
import io.florianlopes.usersmanagement.api.common.web.dto.PageDto;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.SearchUsersQuery;
import io.florianlopes.usersmanagement.api.users.domain.query.UserFilter;
import io.florianlopes.usersmanagement.api.users.domain.usecase.SearchUsersUseCase;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.UserDto;
import io.florianlopes.usersmanagement.api.users.web.mapper.UserMapper;

@RestController
@RequestMapping("/v1/users")
public class SearchUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUsersController.class);

    private final SearchUsersUseCase searchUsersUseCase;
    private final UserMapper userMapper;
    private final PageDtoMapper pageDtoMapper;

    public SearchUsersController(
            SearchUsersUseCase searchUsersUseCase,
            UserMapper userMapper,
            PageDtoMapper pageDtoMapper) {
        this.searchUsersUseCase = searchUsersUseCase;
        this.userMapper = userMapper;
        this.pageDtoMapper = pageDtoMapper;
    }

    @GetMapping("/search")
    public PageDto<UserDto> searchUsers(
            Pageable pageable,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {
        LOGGER.info(
                "Received search request with params firstName={}, lastName={}, email={}",
                firstName,
                lastName,
                email);
        final Page<User> users =
                this.searchUsersUseCase.searchUsers(
                        new SearchUsersQuery(pageable, new UserFilter(firstName, lastName, email)));
        final Page<UserDto> usersDto = users.map(this.userMapper::asUserDto);

        LOGGER.info("Found {} users matching search", usersDto.getTotalElements());
        return this.pageDtoMapper.asPageDto(usersDto);
    }
}
