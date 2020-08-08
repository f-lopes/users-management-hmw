package io.florianlopes.usersmanagement.api.users.domain.listener;

import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationFailureEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;

public interface CreateUserListener {

    void onSuccess(UserCreationSuccessEvent userEvent);

    void onFailure(UserCreationFailureEvent userEvent);
}
