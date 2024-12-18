package com.mango.customer.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.mango.customer.infrastructure.adapters.persistence")
public class MongoConfig {
}
