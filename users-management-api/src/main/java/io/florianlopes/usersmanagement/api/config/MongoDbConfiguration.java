package io.florianlopes.usersmanagement.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
// Only enable MongoDB transactions in a real production environment as transactions are not
// supported without a replica set
@ConditionalOnProperty(value = "mongo.transactions.enabled", havingValue = "true")
class MongoDbConfiguration extends AbstractMongoClientConfiguration {

    private final String databaseName;

    public MongoDbConfiguration(@Value("${database.name}") String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    protected String getDatabaseName() {
        return this.databaseName;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
