package io.florianlopes.usersmanagement.api.users.web.mapper;

import org.mapstruct.Mapper;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.UserDto;

@Mapper(componentModel = "spring", implementationName = "WebUserMapper")
public interface UserMapper {

    UserDto asUserDto(User user);
}
