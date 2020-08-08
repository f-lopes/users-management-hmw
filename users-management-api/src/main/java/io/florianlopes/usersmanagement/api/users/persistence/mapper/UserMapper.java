package io.florianlopes.usersmanagement.api.users.persistence.mapper;

import org.mapstruct.Mapper;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.persistence.entity.UserDocument;

@Mapper(componentModel = "spring", implementationName = "PersistenceUserMapper")
public interface UserMapper {

    User asUser(UserDocument userDocument);

    UserDocument asUserDocument(User user);
}
