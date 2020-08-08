package io.florianlopes.usersmanagement.api.users.domain.usecase;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Transactional
class CreateUserService implements CreateUserUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    private final UserAdapter userAdapter;
    private final List<String> authorizedCountriesCodes;
    private final IpApiService ipApiService;
    private final List<CreateUserListener> createUserListeners;
    private final boolean ipCheckEnabled;

    public CreateUserService(
            UserAdapter userAdapter,
            @Value("${app.user.creation.authorizedCountries}")
                    List<String> authorizedCountriesCodes,
            IpApiService ipApiService,
            List<CreateUserListener> createUserListeners,
            @Value("${app.user.creation.ip.check.enabled}") boolean ipCheckEnabled) {
        this.userAdapter = userAdapter;
        this.authorizedCountriesCodes = authorizedCountriesCodes;
        this.ipApiService = ipApiService;
        this.createUserListeners = createUserListeners;
        this.ipCheckEnabled = ipCheckEnabled;
    }

    @Override
    public User createUser(CreateUserCommand createUserCommand) throws UserCreationException {
        try {
            LOGGER.info("Creating user {}", createUserCommand);
            final User createdUser = doCreateUser(createUserCommand);
            LOGGER.info("Successfully created user {}", createdUser);
            notifyListenersOnSuccess(toUserCreationSuccessEvent(createdUser));
            return createdUser;
        } catch (UserCreationException uce) {
            LOGGER.error("Failed to create user {}", createUserCommand);
            notifyListenersOnFailure(toUserCreationFailureEvent(createUserCommand));
            throw uce;
        }
    }

    private User doCreateUser(CreateUserCommand createUserCommand) throws UserCreationException {
        if (ipCheckEnabled) {
            if (StringUtils.isBlank(createUserCommand.getIpAddress())) {
                throw new InvalidIpAddressUserCreationException(
                        "Could not create user: IP address is blank");
            }
            checkClientIp(createUserCommand);
        }
        return this.userAdapter.createUser(toUser(createUserCommand));
    }

    private void checkClientIp(CreateUserCommand createUserCommand) throws UserCreationException {
        try {
            final String countryCode =
                    this.ipApiService.getCountryCodeByIp(createUserCommand.getIpAddress());
            if (!this.authorizedCountriesCodes.contains(countryCode)) {
                throw new UnauthorizedUserCreationException(
                        "Could not create user: IP address is not authorized");
            }
        } catch (IpApiException iae) {
            throw new UserCreationException(
                    "Could not create user: could not retrieve country from IP address", iae);
        }
    }

    private User toUser(CreateUserCommand createUserCommand) {
        return new User(
                createUserCommand.getFirstName(),
                createUserCommand.getLastName(),
                createUserCommand.getEmail(),
                createUserCommand.getPassword());
    }

    private void notifyListenersOnSuccess(UserCreationSuccessEvent userEvent) {
        LOGGER.info("Notifying listeners on success {}", userEvent);
        this.createUserListeners.forEach(
                createUserListener -> {
                    LOGGER.debug(
                            "Notifying listener {} with event {}", createUserListener, userEvent);
                    createUserListener.onSuccess(userEvent);
                });
    }

    private void notifyListenersOnFailure(UserCreationFailureEvent userEvent) {
        LOGGER.info("Notifying listeners on failure {}", userEvent);
        this.createUserListeners.forEach(
                createUserListener -> {
                    LOGGER.debug(
                            "Notifying listener {} with event {}", createUserListener, userEvent);
                    createUserListener.onFailure(userEvent);
                });
    }

    private UserCreationSuccessEvent toUserCreationSuccessEvent(User createdUser) {
        return new UserCreationSuccessEvent(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getEmail(),
                createdUser.getPassword());
    }

    private UserCreationFailureEvent toUserCreationFailureEvent(
            CreateUserCommand createUserCommand) {
        return new UserCreationFailureEvent(
                createUserCommand.getFirstName(),
                createUserCommand.getLastName(),
                createUserCommand.getEmail(),
                createUserCommand.getPassword());
    }
}
