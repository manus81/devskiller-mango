package com.mango.customer.application.user;

import com.mango.customer.application.exceptions.UserNotFoundException;
import com.mango.customer.domain.model.User;
import com.mango.customer.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setId("user123");
		user.setName("John Doe");
		user.setCity("New York");
		user.setAddress("123 Street");
		user.setEmail("john.doe@example.com");
	}

	@Test
	public void testGetAll() {
		when(userRepository.findAll()).thenReturn(Flux.just(user));

		Flux<User> result = userService.getAll();

		StepVerifier.create(result)
			.expectNext(user)
			.verifyComplete();

		verify(userRepository).findAll();
	}

	@Test
	public void testGetById_UserExists() {
		when(userRepository.findById("user123")).thenReturn(Mono.just(user));

		Mono<User> result = userService.getById("user123");

		StepVerifier.create(result)
			.expectNext(user)
			.verifyComplete();

		verify(userRepository).findById("user123");
	}

	@Test
	public void testGetById_UserNotFound() {
		when(userRepository.findById("user123")).thenReturn(Mono.empty());

		Mono<User> result = userService.getById("user123");

		StepVerifier.create(result)
			.expectErrorMatches(throwable ->
				throwable instanceof UserNotFoundException &&
					throwable.getMessage().equals("User: user123 not found")
			)
			.verify();

		verify(userRepository).findById("user123");
	}

	@Test
	public void testCreateUser() {
		when(userRepository.save(user)).thenReturn(Mono.just(user));

		Mono<User> result = userService.createUser(user);

		StepVerifier.create(result)
			.expectNext(user)
			.verifyComplete();

		verify(userRepository).save(user);
	}

	@Test
	public void testUpdateUser_UserExists() {
		User updatedUser = new User();
		updatedUser.setId("user123");
		updatedUser.setName("Jane Doe");
		updatedUser.setCity("Los Angeles");
		updatedUser.setAddress("456 Avenue");
		updatedUser.setEmail("jane.doe@example.com");

		when(userRepository.findById("user123")).thenReturn(Mono.just(user));
		when(userRepository.save(user)).thenReturn(Mono.just(updatedUser));

		Mono<User> result = userService.updateUser(updatedUser);

		StepVerifier.create(result)
			.expectNext(updatedUser)
			.verifyComplete();

		verify(userRepository).findById("user123");
		verify(userRepository).save(updatedUser);
	}

	@Test
	public void testUpdateUser_UserNotFound() {
		when(userRepository.findById("user123")).thenReturn(Mono.empty());

		Mono<User> result = userService.updateUser(user);

		StepVerifier.create(result)
			.expectErrorMatches(throwable ->
				throwable instanceof UserNotFoundException &&
					throwable.getMessage().equals("User: user123 not found")
			)
			.verify();

		verify(userRepository).findById("user123");
		verify(userRepository, never()).save(any());
	}

	@Test
	public void testDeleteUser() {
		when(userRepository.deleteById("user123")).thenReturn(Mono.empty());

		Mono<Void> result = userService.deleteUser("user123");

		StepVerifier.create(result)
			.verifyComplete();

		verify(userRepository).deleteById("user123");
	}
}
