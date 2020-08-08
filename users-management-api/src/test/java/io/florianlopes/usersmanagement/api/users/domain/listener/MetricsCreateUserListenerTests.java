package io.florianlopes.usersmanagement.api.users.domain.listener;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.florianlopes.usersmanagement.api.users.domain.Metrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class MetricsCreateUserListenerTests {

    // Can't mock without using PowerMock or PowerMockito (abstract class)
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    private MetricsCreateUserListener metricsCreateUserListener;

    @BeforeEach
    void setUp() {
        this.metricsCreateUserListener = new MetricsCreateUserListener(this.meterRegistry);
    }

    @Test
    void onSuccessShouldIncrementSuccessfullUsersCreationsCounter() {
        final Counter successfulUsersCreationsCounter =
                this.meterRegistry.counter(Metrics.NUMBER_OF_SUCCESSFUL_USER_CREATIONS);
        Assumptions.assumeThat(successfulUsersCreationsCounter.count()).isEqualTo(0);

        this.metricsCreateUserListener.onSuccess(null);

        assertThat(successfulUsersCreationsCounter.count()).isEqualTo(1);
    }

    @Test
    void onFailureShouldIncrementUnsuccessfullUsersCreationsCounter() {
        final Counter unsuccessfulUsersCreationsCounter =
                this.meterRegistry.counter(Metrics.NUMBER_OF_UNSUCCESSFUL_USER_CREATIONS);
        Assumptions.assumeThat(unsuccessfulUsersCreationsCounter.count()).isEqualTo(0);

        this.metricsCreateUserListener.onFailure(null);

        assertThat(unsuccessfulUsersCreationsCounter.count()).isEqualTo(1);
    }
}
