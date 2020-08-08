package io.florianlopes.usersmanagement.api.users.domain.listener;

import org.springframework.stereotype.Component;

import io.florianlopes.usersmanagement.api.users.domain.Metrics;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationFailureEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
class MetricsCreateUserListener implements CreateUserListener {

    private final Counter successfulUsersCreationsCounter;
    private final Counter unsuccessfulUsersCreationsCounter;

    public MetricsCreateUserListener(MeterRegistry meterRegistry) {
        // Init counters
        this.successfulUsersCreationsCounter =
                meterRegistry.counter(Metrics.NUMBER_OF_SUCCESSFUL_USER_CREATIONS);
        this.unsuccessfulUsersCreationsCounter =
                meterRegistry.counter(Metrics.NUMBER_OF_UNSUCCESSFUL_USER_CREATIONS);
    }

    @Override
    public void onSuccess(UserCreationSuccessEvent userCreationSuccessEvent) {
        this.successfulUsersCreationsCounter.increment();
    }

    @Override
    public void onFailure(UserCreationFailureEvent userCreationFailureEvent) {
        this.unsuccessfulUsersCreationsCounter.increment();
    }
}
