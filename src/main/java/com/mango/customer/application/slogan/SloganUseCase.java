package com.mango.customer.application.slogan;

import com.mango.customer.domain.model.User;
import reactor.core.publisher.Mono;

public interface SloganUseCase {

  Mono<User> createSlogan(SloganDto sloganDto);

}
