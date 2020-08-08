package io.florianlopes.usersmanagement.api.users.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import io.florianlopes.usersmanagement.api.users.domain.model.User;
import io.florianlopes.usersmanagement.api.users.domain.query.UserFilter;
import io.florianlopes.usersmanagement.api.users.persistence.entity.UserDocument;
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

    @Test
    void getAllUsersWithPageOfOneShouldReturnOneUser() {
        this.userRepository.deleteAll();
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@email.com", "test"));
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@email.com", "test"));

        Assumptions.assumeThat(this.userRepository.count()).isEqualTo(2);

        final Page<User> users = this.mongoUserAdapter.getAllUsers(PageRequest.of(1, 1));

        assertThat(users.getSize()).isEqualTo(1);
    }

    @Test
    void getUsersWithNullFilterShouldReturnAllUsers() {
        this.userRepository.deleteAll();
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@email.com", "test"));
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@domain.com", "test"));
        this.userRepository.insert(
                new UserDocument(
                        "FirstName", "LastName", "firstName.lastName@otherdomain.com", "test"));

        Assumptions.assumeThat(this.userRepository.count()).isEqualTo(3);

        final Page<User> users = this.mongoUserAdapter.getUsers(PageRequest.of(1, 10), null);

        assertThat(users.getTotalElements()).isEqualTo(3);
    }

    @ParameterizedTest
    @CsvSource({
        ", , , 3",
        "test, test, test, 0",
        "John, , , 2",
        "John, Doe, , 2",
        "John, Doe, john.doe@email.com, 1",
        ", Doe, , 2",
        "FirstName, , , 1"
    })
    void getUsersWithFilterShouldReturnFilteredUsers(
            String firstNameFilter,
            String lastNameFilter,
            String emailFilter,
            String expectedNbOfResults) {
        final UserFilter userFilter = new UserFilter(firstNameFilter, lastNameFilter, emailFilter);

        this.userRepository.deleteAll();
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@email.com", "test"));
        this.userRepository.insert(new UserDocument("John", "Doe", "john.doe@domain.com", "test"));
        this.userRepository.insert(
                new UserDocument(
                        "FirstName", "LastName", "firstName.lastName@otherdomain.com", "test"));

        Assumptions.assumeThat(this.userRepository.count()).isEqualTo(3);

        final Page<User> users = this.mongoUserAdapter.getUsers(PageRequest.of(0, 10), userFilter);

        assertThat(users.getTotalElements()).isEqualTo(Long.valueOf(expectedNbOfResults));
    }
}
