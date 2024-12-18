package com.mango.customer.infrastructure.adapters.web;

import com.mango.customer.application.slogan.SloganDto;
import com.mango.customer.application.slogan.SloganUseCase;
import com.mango.customer.application.user.UserUseCase;
import com.mango.customer.domain.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserUseCase userUseCase;
  private final SloganUseCase sloganUseCase;

  @GetMapping
  public Flux<User> getAll() {
    return userUseCase.getAll();
  }

  @GetMapping("/{userId}")
  public Mono<User> getById(@PathVariable("id") String id) {
    return userUseCase.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<User> createUser(@RequestBody User user) {
    return userUseCase.createUser(user);
  }

  @PutMapping
  public Mono<User> updateUser(@RequestBody User user) {
    return userUseCase.updateUser(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteUser(@PathVariable("id") String id) {
    return userUseCase.deleteUser(id);
  }

  @PostMapping("/slogan")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<User> createSlogan(@Valid @RequestBody SloganDto sloganDto) {
    return sloganUseCase.createSlogan(sloganDto);
  }
}
