package com.mango.customer.application.user;

import com.mango.customer.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserUseCase {

  Flux<User> getAll();

  Mono<User> getById(String id);

  Mono<User> createUser(User user);

  Mono<User> updateUser(User user);

  Mono<Void> deleteUser(String id);
}
