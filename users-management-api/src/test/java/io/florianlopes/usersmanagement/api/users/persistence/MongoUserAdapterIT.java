package io.florianlopes.usersmanagement.api.users.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.persistence.mapper.PersistenceUserMapper;
import io.florianlopes.usersmanagement.api.users.persistence.mapper.UserMapper;
import io.florianlopes.usersmanagement.api.users.persistence.repository.UserRepository;

@DataMongoTest
@Import(PersistenceUserMapper.class)
class MongoUserAdapterIT {

    @Autowired private UserRepository userRepository;

    @Autowired private UserMapper userMapper;

    private MongoUserAdapter mongoUserAdapter;

    @BeforeEach
    void setUp() {
        this.mongoUserAdapter = new MongoUserAdapter(this.userRepository, this.userMapper);
    }

    @Test
    void testCreateUser() {
        this.userRepository.deleteAll();
        Assumptions.assumeThat(this.userRepository.count()).isEqualTo(0);

        final User userToCreate = new User("John", "Doe", "john.doe@email.com", "test");

        final User createdUser = this.mongoUserAdapter.createUser(userToCreate);

        assertThat(createdUser.getId()).isNotBlank();
        assertThat(createdUser.getFirstName()).isEqualTo("John");
        assertThat(createdUser.getLastName()).isEqualTo("Doe");
        assertThat(createdUser.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(createdUser.getPassword()).isEqualTo("test");

        assertThat(this.userRepository.count()).isEqualTo(1);
    }
}
