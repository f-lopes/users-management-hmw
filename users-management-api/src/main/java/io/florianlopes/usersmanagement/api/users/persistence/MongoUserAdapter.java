package io.florianlopes.usersmanagement.api.users.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.UserFilter;
import io.florianlopes.usersmanagement.api.users.persistence.entity.QUserDocument;
import io.florianlopes.usersmanagement.api.users.persistence.entity.UserDocument;
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

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        final Page<UserDocument> userDocuments = this.userRepository.findAll(pageable);
        return userDocuments.map(this.userMapper::asUser);
    }

    @Override
    public Optional<User> getUserById(String id) {
        final Optional<UserDocument> userDocument = this.userRepository.findById(id);
        return userDocument.map(this.userMapper::asUser);
    }

    @Override
    public Page<User> getUsers(Pageable pageable, UserFilter userFilter) {
        if (userFilter == null) {
            return getAllUsers(pageable);
        }

        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(userFilter.getFirstName())) {
            predicates.add(QUserDocument.userDocument.firstName.eq(userFilter.getFirstName()));
        }
        if (StringUtils.isNotBlank(userFilter.getLastName())) {
            predicates.add(QUserDocument.userDocument.lastName.eq(userFilter.getLastName()));
        }
        if (StringUtils.isNotBlank(userFilter.getEmail())) {
            predicates.add(QUserDocument.userDocument.email.eq(userFilter.getEmail()));
        }

        final Page<UserDocument> userDocuments =
                this.userRepository.findAll(
                        new BooleanBuilder().orAllOf(predicates.toArray(new Predicate[0])),
                        pageable);
        return userDocuments.map(this.userMapper::asUser);
    }
}
