package io.florianlopes.usersmanagement.api.users.domain.listener.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import io.florianlopes.usersmanagement.api.users.domain.listener.CreateUserListener;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationFailureEvent;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;

@Component
public class MessagingCreateUserListener implements CreateUserListener {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public MessagingCreateUserListener(
            RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void onSuccess(UserCreationSuccessEvent userCreationSuccessEvent) {
        this.rabbitTemplate.convertAndSend(
                this.messagingProperties.getDirectExchangeName(),
                this.messagingProperties.getSuccessfulCreatedUsersRoutingKey(),
                userCreationSuccessEvent);
    }

    @Override
    public void onFailure(UserCreationFailureEvent userCreationFailureEvent) {
        this.rabbitTemplate.convertAndSend(
                this.messagingProperties.getDirectExchangeName(),
                this.messagingProperties.getUnsuccessfulCreatedUsersRoutingKey(),
                userCreationFailureEvent);
    }
}
