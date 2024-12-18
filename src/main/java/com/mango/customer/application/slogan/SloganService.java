package com.mango.customer.application.slogan;

import com.mango.customer.application.exceptions.UserNotFoundException;
import com.mango.customer.domain.model.User;
import com.mango.customer.domain.ports.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SloganService implements SloganUseCase {

  private UserRepository userRepository;

  @Override
  public Mono<User> createSlogan(SloganDto sloganDto) {
    return userRepository.findById(sloganDto.getUserId())
        .switchIfEmpty(Mono.error(new UserNotFoundException(
            "User: " + sloganDto.getUserId() + " not found"
        )))
        .flatMap(user -> {
          user.addSlogan(sloganDto.getSlogan());
          return userRepository.save(user);
        });
  }
}
