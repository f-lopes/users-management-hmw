package io.florianlopes.usersmanagement.api.users.persistence;

import org.springframework.stereotype.Repository;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.persistence.mapper.UserMapper;
import io.florianlopes.usersmanagement.api.users.persistence.repository.UserRepository;

@Repository
class MongoUserAdapter implements UserAdapter {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public MongoUserAdapter(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        return this.userMapper.asUser(
                this.userRepository.insert(this.userMapper.asUserDocument(user)));
    }
}
