package io.florianlopes.usersmanagement.api.users.domain.listener;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationFailureEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.messaging.MessagingCreateUserListener;
import io.florianlopes.usersmanagement.api.users.domain.listener.messaging.MessagingProperties;

@ExtendWith(MockitoExtension.class)
class MessagingCreateUserListenerTests {

    @Mock private RabbitTemplate rabbitTemplate;

    private MessagingCreateUserListener messagingCreateUserListener;
    private MessagingProperties messagingProperties;

    @BeforeEach
    void setUp() {
        this.messagingProperties = new MessagingProperties();
        this.messagingProperties.setDirectExchangeName("direct-exchange");
        this.messagingProperties.setSuccessfulCreatedUsersRoutingKey("users.creations.success");
        this.messagingProperties.setUnsuccessfulCreatedUsersRoutingKey("users.creations.failure");
        this.messagingCreateUserListener =
                new MessagingCreateUserListener(this.rabbitTemplate, messagingProperties);
    }

    @Test
    void shouldSendEventInSuccessQueue() {
        final UserCreationSuccessEvent userCreationSuccessEvent =
                new UserCreationSuccessEvent("", "john.doe@email.com", "John", "Doe", "test");
        this.messagingCreateUserListener.onSuccess(userCreationSuccessEvent);

        verify(this.rabbitTemplate)
                .convertAndSend(
                        exchangeName(),
                        successfulCreatedUsersRoutingKey(),
                        userCreationSuccessEvent);
    }

    @Test
    void shouldSendEventInFailureQueue() {
        final UserCreationFailureEvent userCreationFailureEvent =
                new UserCreationFailureEvent("john.doe@email.com", "John", "Doe", "test");
        this.messagingCreateUserListener.onFailure(userCreationFailureEvent);

        verify(this.rabbitTemplate)
                .convertAndSend(
                        exchangeName(),
                        unsuccessfulCreatedUsersRoutingKey(),
                        userCreationFailureEvent);
    }

    private String unsuccessfulCreatedUsersRoutingKey() {
        return this.messagingProperties.getUnsuccessfulCreatedUsersRoutingKey();
    }

    private String successfulCreatedUsersRoutingKey() {
        return this.messagingProperties.getSuccessfulCreatedUsersRoutingKey();
    }

    private String exchangeName() {
        return this.messagingProperties.getDirectExchangeName();
    }
}
