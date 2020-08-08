package io.florianlopes.usersmanagement.api.users.domain.listener.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messaging")
public class MessagingProperties {

    private String successfulCreatedUsersQueueName;
    private String unsuccessfulCreatedUsersQueueName;
    private String directExchangeName;
    private String successfulCreatedUsersRoutingKey;
    private String unsuccessfulCreatedUsersRoutingKey;

    public String getSuccessfulCreatedUsersQueueName() {
        return successfulCreatedUsersQueueName;
    }

    public void setSuccessfulCreatedUsersQueueName(String successfulCreatedUsersQueueName) {
        this.successfulCreatedUsersQueueName = successfulCreatedUsersQueueName;
    }

    public String getUnsuccessfulCreatedUsersQueueName() {
        return unsuccessfulCreatedUsersQueueName;
    }

    public void setUnsuccessfulCreatedUsersQueueName(String unsuccessfulCreatedUsersQueueName) {
        this.unsuccessfulCreatedUsersQueueName = unsuccessfulCreatedUsersQueueName;
    }

    public String getDirectExchangeName() {
        return directExchangeName;
    }

    public void setDirectExchangeName(String directExchangeName) {
        this.directExchangeName = directExchangeName;
    }

    public String getSuccessfulCreatedUsersRoutingKey() {
        return successfulCreatedUsersRoutingKey;
    }

    public void setSuccessfulCreatedUsersRoutingKey(String successfulCreatedUsersRoutingKey) {
        this.successfulCreatedUsersRoutingKey = successfulCreatedUsersRoutingKey;
    }

    public String getUnsuccessfulCreatedUsersRoutingKey() {
        return unsuccessfulCreatedUsersRoutingKey;
    }

    public void setUnsuccessfulCreatedUsersRoutingKey(String unsuccessfulCreatedUsersRoutingKey) {
        this.unsuccessfulCreatedUsersRoutingKey = unsuccessfulCreatedUsersRoutingKey;
    }
}
