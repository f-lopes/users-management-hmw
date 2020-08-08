package io.florianlopes.usersmanagement.api.config.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.florianlopes.usersmanagement.api.users.domain.listener.messaging.MessagingProperties;

@Configuration
public class MessagingConfig {

    private final MessagingProperties messagingProperties;

    public MessagingConfig(MessagingProperties messagingProperties) {
        this.messagingProperties = messagingProperties;
    }

    @Bean
    Queue usersCreationsSuccessQueues() {
        return new Queue(this.messagingProperties.getSuccessfulCreatedUsersQueueName(), true);
    }

    @Bean
    Queue usersCreationsFailureQueues() {
        return new Queue(this.messagingProperties.getUnsuccessfulCreatedUsersQueueName(), true);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(this.messagingProperties.getDirectExchangeName());
    }

    @Bean
    Binding successQueueBinding(Queue usersCreationsSuccessQueues, DirectExchange directExchange) {
        return BindingBuilder.bind(usersCreationsSuccessQueues)
                .to(directExchange)
                .with(this.messagingProperties.getSuccessfulCreatedUsersRoutingKey());
    }

    @Bean
    Binding failureQueueBinding(Queue usersCreationsFailureQueues, DirectExchange directExchange) {
        return BindingBuilder.bind(usersCreationsFailureQueues)
                .to(directExchange)
                .with(this.messagingProperties.getUnsuccessfulCreatedUsersRoutingKey());
    }

    @Bean
    AbstractMessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //        container.setQueueNames(queueName);
        return container;
    }
}
