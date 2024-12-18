package com.mango.customer.domain.ports;

import com.mango.customer.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
  Mono<User> findById(String id);
  Flux<User> findAll();
  Mono<User> save(User user);
  Mono<Void> deleteById(String id);
}
