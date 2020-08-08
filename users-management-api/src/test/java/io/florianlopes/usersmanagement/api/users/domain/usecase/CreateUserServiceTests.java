package io.florianlopes.usersmanagement.api.users.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.florianlopes.usersmanagement.api.users.domain.adapter.UserAdapter;
import io.florianlopes.usersmanagement.api.users.domain.command.CreateUserCommand;
import io.florianlopes.usersmanagement.api.users.domain.exception.InvalidIpAddressUserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.exception.UnauthorizedUserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.exception.UserCreationException;
import io.florianlopes.usersmanagement.api.users.domain.ipapi.IpApiService;
import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.IpApiException;
import io.florianlopes.usersmanagement.api.users.domain.listener.CreateUserListener;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationFailureEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;
import io.florianlopes.usersmanagement.api.users.domain.model.User;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTests {

    private static final List<String> USER_CREATION_AUTHORIZED_COUNTRIES =
            Collections.singletonList("France");

    @Mock private UserAdapter userAdapter;

    @Mock private IpApiService ipApiService;

    @Mock private CreateUserListener firstCreateUserListener;

    @Mock private CreateUserListener secondCreateUserListener;

    private List<CreateUserListener> createUserListeners;

    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        this.createUserListeners = Arrays.asList(firstCreateUserListener, secondCreateUserListener);

        this.createUserService =
                new CreateUserService(
                        userAdapter,
                        USER_CREATION_AUTHORIZED_COUNTRIES,
                        ipApiService,
                        createUserListeners,
                        true);
    }

    @Test
    void createUserUnavailableIpApiServiceShouldThrowUserCreationException() throws IpApiException {
        final CreateUserCommand createUserCommand =
                new CreateUserCommand("", "", "", "", "80.80.80.80");
        when(this.ipApiService.getCountryCodeByIp(anyString()))
                .thenThrow(new IpApiException("ipapi service unavailable"));

        assertThatExceptionOfType(UserCreationException.class)
                .isThrownBy(() -> this.createUserService.createUser(createUserCommand));

        // Ensure that all the registered listeners has been called on failure
        this.createUserListeners.forEach(
                createUserListener ->
                        verify(createUserListener).onFailure(any(UserCreationFailureEvent.class)));
    }

    @ParameterizedTest(name = "ipAddress")
    @NullAndEmptySource
    void createUserEmptyIpAddressShouldThrowInvalidIpAddressUserCreationException(
            String ipAddress) {
        final CreateUserCommand createUserCommand =
                new CreateUserCommand("John", "Doe", "john.doe@email.com", "test", ipAddress);

        assertThatExceptionOfType(InvalidIpAddressUserCreationException.class)
                .isThrownBy(() -> this.createUserService.createUser(createUserCommand));

        // Ensure that all the registered listeners has been called on failure
        this.createUserListeners.forEach(
                createUserListener ->
                        verify(createUserListener).onFailure(any(UserCreationFailureEvent.class)));
    }

    @Test
    void createUserIpAddressNotFromFranceShouldThrowUnauthorizedUserCreationException()
            throws IpApiException {
        Assumptions.assumeThat("France").isIn(USER_CREATION_AUTHORIZED_COUNTRIES);

        final CreateUserCommand createUserCommand =
                new CreateUserCommand("John", "Doe", "john.doe@email.com", "test", "90.90.90.90");
        when(this.ipApiService.getCountryCodeByIp(createUserCommand.getIpAddress()))
                .thenReturn("Swiss");

        assertThatExceptionOfType(UnauthorizedUserCreationException.class)
                .isThrownBy(() -> this.createUserService.createUser(createUserCommand));

        // Ensure that all the registered listeners has been called on failure
        this.createUserListeners.forEach(
                createUserListener ->
                        verify(createUserListener).onFailure(any(UserCreationFailureEvent.class)));
    }

    @Test
    void createUserIpAddressFromFranceShouldCreateUser()
            throws UserCreationException, IpApiException {
        Assumptions.assumeThat("France").isIn(USER_CREATION_AUTHORIZED_COUNTRIES);

        final CreateUserCommand createUserCommand =
                new CreateUserCommand("John", "Doe", "john.doe@email.com", "test", "90.90.90.90");
        when(this.ipApiService.getCountryCodeByIp(createUserCommand.getIpAddress()))
                .thenReturn("France");
        final User user = new User("John", "Doe", "john.doe@email.com", "test");
        when(this.userAdapter.createUser(user))
                .thenReturn(new User("1234", "John", "Doe", "john.doe@email.com", "test"));

        final User createdUser = this.createUserService.createUser(createUserCommand);

        assertThat(createdUser).isNotNull().isEqualToIgnoringGivenFields(createUserCommand, "id");
        assertThat(createdUser.getId()).isNotBlank().isNotEqualTo(StringUtils.EMPTY);

        verify(this.userAdapter).createUser(user);

        // Ensure that all the registered listeners has been called on success
        this.createUserListeners.forEach(
                createUserListener ->
                        verify(createUserListener).onSuccess(any(UserCreationSuccessEvent.class)));
    }

    @Test
    void createUserIpCheckDisabledShouldCreateUser() throws UserCreationException, IpApiException {
        final CreateUserCommand createUserCommand =
                new CreateUserCommand("John", "Doe", "john.doe@email.com", "test", "90.90.90.90");
        final User user = new User("John", "Doe", "john.doe@email.com", "test");
        when(this.userAdapter.createUser(user))
                .thenReturn(new User("1234", "John", "Doe", "john.doe@email.com", "test"));

        final CreateUserUseCase createUserService =
                new CreateUserService(
                        userAdapter,
                        USER_CREATION_AUTHORIZED_COUNTRIES,
                        ipApiService,
                        createUserListeners,
                        false);
        final User createdUser = createUserService.createUser(createUserCommand);

        assertThat(createdUser).isNotNull().isEqualToIgnoringGivenFields(createUserCommand, "id");
        assertThat(createdUser.getId()).isNotBlank().isNotEqualTo(StringUtils.EMPTY);

        verify(this.userAdapter).createUser(user);
        verify(this.ipApiService, never()).getCountryCodeByIp(anyString());

        // Ensure that all the registered listeners has been called on success
        this.createUserListeners.forEach(
                createUserListener ->
                        verify(createUserListener).onSuccess(any(UserCreationSuccessEvent.class)));
    }
}
