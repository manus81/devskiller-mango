package com.mango.customer.application.user;

import com.mango.customer.application.exceptions.UserNotFoundException;
import com.mango.customer.domain.model.User;
import com.mango.customer.domain.ports.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService implements UserUseCase {

	private final UserRepository userRepository;

	public Flux<User> getAll() {
		return userRepository.findAll();
	}

	public Mono<User> getById(String id) {
		return userRepository.findById(id)
			.switchIfEmpty(Mono.error(new UserNotFoundException("User: " + id + " not found")));
	}

	public Mono<User> createUser(User user) {
		return userRepository.save(user);
	}

	public Mono<User> updateUser(User user) {
		return Mono.justOrEmpty(user.getId())
			.switchIfEmpty(Mono.error(new UserNotFoundException("User ID must not be null")))
			.flatMap(userId -> userRepository.findById(userId)
				.switchIfEmpty(Mono.error(new UserNotFoundException("User: " + userId + " not found")))
				.flatMap(dbUser -> {
					dbUser.setName(user.getName());
					dbUser.setCity(user.getCity());
					dbUser.setAddress(user.getAddress());
					dbUser.setEmail(user.getEmail());
					return userRepository.save(dbUser);
				})
			);
	}

	public Mono<Void> deleteUser(String id) {
		return userRepository.deleteById(id);
	}
}
