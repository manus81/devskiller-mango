package com.mango.customer.infrastructure.config;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
public class MongoTestConfig {

	private MongodExecutable mongodExecutable;
	private MongodProcess mongodProcess;

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
		IMongodConfig mongodConfig = (new MongodConfigBuilder())
			.version(de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION)
			.net(new Net("localhost", 27017, false))
			.build();

		mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
		mongodProcess = mongodExecutable.start();

		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

		return new ReactiveMongoTemplate(new SimpleReactiveMongoDatabaseFactory(mongoClient, "test"));
	}

	public void stopMongo() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (mongodProcess != null) {
				mongodProcess.stop();
			}
		}));
	}
}
