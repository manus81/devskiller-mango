package com.mango.customer.infrastructure.adapters.persistence;

import com.mango.customer.domain.model.User;
import com.mango.customer.domain.ports.UserRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MongoUserRepository extends UserRepository, ReactiveMongoRepository<User, String> {

  @Override
  Mono<User> findById(String id);

  @Override
  Flux<User> findAll();

  @Override
  Mono<User> save(User user);

  @Override
  Mono<Void> deleteById(String id);
}
